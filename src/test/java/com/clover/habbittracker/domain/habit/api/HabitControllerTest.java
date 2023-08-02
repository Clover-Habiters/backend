package com.clover.habbittracker.domain.habit.api;

import static com.clover.habbittracker.global.exception.ErrorType.*;
import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.habitcheck.dto.HabitCheckRequest;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.habitcheck.repository.HabitCheckRepository;

public class HabitControllerTest extends RestDocsSupport {

	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private HabitCheckRepository habitCheckRepository;
	private Habit testHabit;

	@BeforeEach
	void setUp() {
		Habit habit = Habit.builder().content("미리 저장된 습관입니다.").member(savedMember).build();
		testHabit = habitRepository.save(habit);
	}

	@Test
	@DisplayName("사용자는 습관정보를 등록 할 수 있다.")
	void createHabitTest() throws Exception {
		//given
		HabitRequest habitRequest = new HabitRequest("테스트 습관입니다.");
		String request = objectMapper.writeValueAsString(habitRequest);

		//when
		mockMvc.perform(
				post("/habits")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("content").type(STRING)
						.description("등록할 습관 내용")
						.attributes(field("constraints", "10자 이내"))
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data").type(NUMBER).description("등록된 습관 아이디")
				)));
	}

	@Test
	@DisplayName("사용자는 습관 정보를 업데이트 할 수 있다.")
	void updateHabitTest() throws Exception {
		//given
		HabitRequest habitRequest = new HabitRequest("수정된 습관입니다.");
		String request = objectMapper.writeValueAsString(habitRequest);

		//when
		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/habits/{habitId}", testHabit.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.content").exists())
			.andExpect(jsonPath("$.data.createDate").exists())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("habitId").description("수정할 습관 아이디")
				),
				requestFields(
					fieldWithPath("content").description("수정할 습관 내용").attributes(field("constraints", "10자 이내"))
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("습관 아이디"),
					fieldWithPath("data.content").type(STRING).description("습관 내용"),
					fieldWithPath("data.createDate").type(STRING).description("습관 등록 날짜")
				)
			));
	}

	@Test
	@DisplayName("사용자는 습관을 삭제 할 수 있다.")
	void deleteHabitTest() throws Exception {
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/habits/{habitId}", testHabit.getId())
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("habitId").description("삭제할 습관 아이디")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)
			));
	}

	@Test
	@DisplayName("사용자는 저장된 습관 수행 여부를 체크 할 수 있다.")
	void habitCheckTest() throws Exception {
		//given
		LocalDate now = LocalDate.now();
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();
		String request = objectMapper.writeValueAsString(new HabitCheckRequest(today));
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/habits/{habitId}/check", testHabit.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("habitId").description("습관 아이디")
				),
				requestFields(
					fieldWithPath("requestDate").description("습관을 체크하고 싶은 날짜")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)
			));
	}

	@Test
	@DisplayName("사용자는 저장된 습관 수행 여부를 취소 할 수 있다.")
	void habitUnCheckTest() throws Exception {
		HabitCheck toBeErased = HabitCheck.builder().checked(true).habit(testHabit).build();
		habitCheckRepository.save(toBeErased);
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/habits/{habitCheckId}/uncheck", toBeErased.getId())
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("habitCheckId").description("습관체크 아이디")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)
			));
	}

	@Test
	@DisplayName("사용자는 나의 습관 리스트와 습관 수행 여부를 조회 할 수 있다.")
	void myHabitListTest() throws Exception {
		//given
		HabitCheck testHabitCheck = HabitCheck.builder().habit(testHabit).checked(true).build();
		habitCheckRepository.save(testHabitCheck);

		mockMvc.perform(
				get("/habits")
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0].id").exists())
			.andExpect(jsonPath("$.data.[0].content").exists())
			.andExpect(jsonPath("$.data.[0].createDate").exists())
			.andExpect(jsonPath("$.data.[0].habitChecks").isArray())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data[].id").type(NUMBER).description("습관 아이디"),
					fieldWithPath("data[].content").type(STRING).description("습관 내용"),
					fieldWithPath("data[].createDate").type(STRING).description("습관 등록 날짜"),
					fieldWithPath("data[].habitChecks[]").type(ARRAY).description("습관 체크 여부")
				)
			));
	}

	@Test
	@DisplayName("사용자는 나의 습관 리스트와 습관 수행 여부를 월 별로 조회 할 수 있다.")
	void monthlyMyHabitListTest() throws Exception {
		//given
		HabitCheck testHabitCheck = HabitCheck.builder().habit(testHabit).checked(true).build();
		habitCheckRepository.save(testHabitCheck);
		LocalDateTime now = LocalDateTime.now();
		String date = now.getYear() + "-0" + now.getMonthValue();

		//when then
		mockMvc.perform(
				get("/habits")
					.header("Authorization", "Bearer " + accessToken).param("date", date))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0].id").exists())
			.andExpect(jsonPath("$.data.[0].content").exists())
			.andExpect(jsonPath("$.data.[0].createDate").exists())
			.andExpect(jsonPath("$.data.[0].habitChecks").isArray())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				queryParameters(
					parameterWithName("date").description("조회 하고 싶은 연 월").optional()
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data[].id").type(NUMBER).description("습관 아이디"),
					fieldWithPath("data[].content").type(STRING).description("습관 내용"),
					fieldWithPath("data[].createDate").type(STRING).description("습관 등록 날짜"),
					fieldWithPath("data[].habitChecks[]").type(ARRAY).description("습관 체크 여부")
				)
			));
	}

	@Test
	@DisplayName("잘못된 습관 아이디를 보낼 경우 HabitNotFound 예외가 터진다.")
	void habitCheckTestWithWrongId() throws Exception {
		//given
		LocalDate now = LocalDate.now();
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();
		String request = objectMapper.writeValueAsString(new HabitCheckRequest(today));

		long wrongId = 0L;
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/habits/{habitId}/check", wrongId)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errorName", is(HABIT_NOT_FOUND.name())))
			.andExpect(jsonPath("$.msg", is(HABIT_NOT_FOUND.getErrorMsg())))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("habitId").description("잘못된 습관 아이디")
				),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("errorName").type(STRING).description("결과 코드"),
					fieldWithPath("msg").type(STRING).description("결과 메시지")
				)));
	}

	@Test
	@DisplayName("습관 수행 여부를 중복 체크 하면 HabitCheckDuplicate 예외가 터진다.")
	void habitCheckTestWithDuplicate() throws Exception {
		//given
		LocalDate now = LocalDate.now();
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();
		String request = objectMapper.writeValueAsString(new HabitCheckRequest(today));
		//when then
		mockMvc.perform(
			post("/habits/{habitId}/check", testHabit.getId())
				.header("Authorization", "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(request));

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/habits/{habitId}/check", testHabit.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.errorName", is(HABIT_CHECK_DUPLICATE.name())))
			.andExpect(jsonPath("$.msg", is(HABIT_CHECK_DUPLICATE.getErrorMsg())))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("habitId").description("습관 아이디")
				),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("errorName").type(STRING).description("결과 코드"),
					fieldWithPath("msg").type(STRING).description("결과 메시지")
				)));

	}

	@Test
	@DisplayName("습관 수행 여부를 체크 할 경우 과거 또는 미래를 체크 할 경우 HabitExpiredException 예외가 터진다.")
	void habitCheckTestWithExpiredDate() throws Exception {
		//given
		LocalDate yesterday = LocalDate.now().minusDays(1);
		String past = yesterday.getMonthValue() + "-" + yesterday.getDayOfMonth();
		String request = objectMapper.writeValueAsString(new HabitCheckRequest(past));

		//when then
		mockMvc.perform(
			post("/habits/{habitId}/check", testHabit.getId())
				.header("Authorization", "Bearer " + accessToken));

		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/habits/{habitId}/check", testHabit.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorName", is(HABIT_CHECK_EXPIRED.name())))
			.andExpect(jsonPath("$.msg", is(HABIT_CHECK_EXPIRED.getErrorMsg())))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("habitId").description("습관 아이디")
				),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("requestDate").description("습관을 체크하고 싶은 날짜")
				),
				responseFields(
					fieldWithPath("errorName").type(STRING).description("결과 코드"),
					fieldWithPath("msg").type(STRING).description("결과 메시지")
				)));

	}

}

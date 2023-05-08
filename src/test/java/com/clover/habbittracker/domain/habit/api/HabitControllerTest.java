package com.clover.habbittracker.domain.habit.api;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.exception.ErrorType;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class HabitControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private JwtProvider jwtProvider;

	private String accessJwt;

	private Habit testHabit;

	@BeforeEach
	void setUp() {
		Member testMember = createTestMember();
		testHabit = Habit.builder().content("미리 저장된 습관입니다.").member(testMember).build();
		memberRepository.save(testMember);
		habitRepository.save(testHabit);
		accessJwt = jwtProvider.createAccessJwt(getId());
	}

	@Test
	@DisplayName("사용자는 습관정보를 등록 할 수 있다.")
	void createHabitTest() throws Exception {
		//given
		HabitRequest habitRequest = new HabitRequest("테스트 습관입니다.");
		String request = new ObjectMapper().writeValueAsString(habitRequest);

		//when
		mockMvc.perform(
				post("/habits")
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 습관 정보를 업데이트 할 수 있다.")
	void updateHabitTest() throws Exception {
		//given
		HabitRequest habitRequest = new HabitRequest("수정된 습관입니다.");
		String request = new ObjectMapper().writeValueAsString(habitRequest);

		//when
		mockMvc.perform(
				put("/habits/" + testHabit.getId())
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.content").exists())
			.andExpect(jsonPath("$.data.createDate").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 습관을 삭제 할 수 있다.")
	void deleteHabitTest() throws Exception {
		//when then
		mockMvc.perform(
				delete("/habits/" + testHabit.getId())
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isNoContent())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 저장된 습관 수행 여부를 체크 할 수 있다.")
	void habitCheckTest() throws Exception {
		//when then
		mockMvc.perform(
				post("/habits/" + testHabit.getId() + "/check")
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 저장된 습관 수행 여부를 취소 할 수 있다.")
	void habitUnCheckTest() throws Exception {
		//when then
		mockMvc.perform(
				delete("/habits/" + testHabit.getId() + "/check")
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isNoContent())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 나의 습관 리스트와 습관 수행 여부를 조회 할 수 있다.")
	void myHabitListTest() throws Exception {

		mockMvc.perform(
				get("/habits")
					.header("Authorization", "Bearer " + accessJwt)
					.param("date", "2023-04"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.[0].id").exists())
			.andExpect(jsonPath("$.data.[0].content").exists())
			.andExpect(jsonPath("$.data.[0].createDate").exists())
			.andExpect(jsonPath("$.data.[0].habitChecks").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 습관 아이디를 보낼 경우 HabitNotFound 예외가 터진다.")
	void habitCheckTestWithWrongId() throws Exception {
		long wrongId = 0L;
		mockMvc.perform(
				post("/habits/" + wrongId + "/check")
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errorName", is(ErrorType.HABIT_NOT_FOUND.name())))
			.andExpect(jsonPath("$.msg", is(ErrorType.HABIT_NOT_FOUND.getErrorMsg())))
			.andDo(print());
	}

	@Test
	@DisplayName("습관 수행 여부를 중복 체크 하면 HabitCheckDuplicate 예외가 터진다.")
	void habitCheckTestWithExpiredDate() throws Exception {
		//given

		//when then
		mockMvc.perform(
			post("/habits/" + testHabit.getId() + "/check")
				.header("Authorization", "Bearer " + accessJwt));

		mockMvc.perform(
			post("/habits/" + testHabit.getId() + "/check")
				.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.errorName", is(ErrorType.HABIT_CHECK_DUPLICATE.name())))
			.andExpect(jsonPath("$.msg", is(ErrorType.HABIT_CHECK_DUPLICATE.getErrorMsg())))
			.andDo(print());

	}

}

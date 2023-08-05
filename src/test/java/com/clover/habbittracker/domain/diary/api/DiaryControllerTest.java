package com.clover.habbittracker.domain.diary.api;

import static com.clover.habbittracker.global.base.exception.ErrorType.*;
import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.entity.Diary;
import com.clover.habbittracker.domain.diary.repository.DiaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

class DiaryControllerTest extends RestDocsSupport {

	@Autowired
	private DiaryRepository diaryRepository;
	private Diary saveDiary;

	@BeforeEach
	void setUp() {
		Diary diary = Diary.builder()
			.endUpdateDate(LocalDateTime.now().plusHours(24))
			.member(savedMember)
			.content("미리 저장된 테스트 회고록입니다.")
			.build();
		saveDiary = diaryRepository.save(diary);

	}

	@Test
	@DisplayName("사용자는 회고록을 등록 할 수 있다.")
	void createDiaryTest() throws Exception {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("테스트 회고록입니다.");
		String request = new ObjectMapper().writeValueAsString(diaryRequest);

		//when then
		mockMvc.perform(
				post("/diaries")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("content").type(STRING)
						.description("회고록 내용")
						.attributes(field("constraints", "500자 이내"))
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data").type(NUMBER).description("생성된 회고 아이디")
				)));
	}

	@Test
	@DisplayName("사용자는 회고록을 수정 할 수 있다.")
	void updateDiaryTest() throws Exception {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("회고록 수정내용 입니다.");
		String request = new ObjectMapper().writeValueAsString(diaryRequest);
		Long id = saveDiary.getId();
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/diaries/{diaryId}", id)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			// 이 부분은 아래 restDocs로 검증하므로 빼야하는게 맞는걸까요?
			.andExpect(jsonPath("$.data.id", is(saveDiary.getId().intValue())))
			.andExpect(jsonPath("$.data.content", is(diaryRequest.getContent())))
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("diaryId").description("수정할 회고 id")
				),
				requestFields(
					fieldWithPath("content").type(STRING)
						.description("수정할 회고 내용")
						.attributes(field("constraints", "500자 이내"))
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("회고록 아이디"),
					fieldWithPath("data.content").type(STRING).description("회고록 내용"),
					fieldWithPath("data.createDate").type(STRING).description("회고록 등록 날짜"),
					fieldWithPath("data.endUpdateDate").type(STRING).description("회고 수정 마감 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 회고록을 전체 조회 할 수 있다.")
	void getMyDiaryListTest() throws Exception {
		//when then
		mockMvc.perform(
				get("/diaries")
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data[].id").type(NUMBER).description("회고록 아이디"),
					fieldWithPath("data[].content").type(STRING).description("회고록 내용"),
					fieldWithPath("data[].createDate").type(STRING).description("회고 등록 날짜"),
					fieldWithPath("data[].endUpdateDate").type(STRING).description("회고 수정 마감 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 회고록을 월 별로 조회 할 수 있다.")
	void getAMonthlyMyDiaryListTest() throws Exception {
		//given
		LocalDateTime now = LocalDateTime.now();
		String date = now.getYear() + "-0" + now.getMonthValue();

		//when then
		mockMvc.perform(
				get("/diaries")
					.header("Authorization", "Bearer " + accessToken)
					.param("date", date))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				queryParameters(
					parameterWithName("date").description("조회하고 싶은 연 월").optional()
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data[].id").type(NUMBER).description("회고록 아이디"),
					fieldWithPath("data[].content").type(STRING).description("회고록 내용"),
					fieldWithPath("data[].createDate").type(STRING).description("회고 등록 날짜"),
					fieldWithPath("data[].endUpdateDate").type(STRING).description("회고 수정 마감 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 나의 회고록을 삭제 할 수 있다.")
	void deleteDiaryTest() throws Exception {
		Long id = saveDiary.getId();
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/diaries/{diaryId}", id)
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("diaryId").description("삭제할 회고 아이디")
				),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)));
	}

	@Test
	@DisplayName("24시간이 지난 회고록은 수정 할 수 없다.")
	void updateDiaryWithExpiredDateTest() throws Exception {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("회고록 수정내용 입니다.");
		String request = new ObjectMapper().writeValueAsString(diaryRequest);
		Diary expiredDiary = Diary.builder()
			.endUpdateDate(LocalDateTime.now().minusDays(24))
			.content("마감시간이 지난 회고록")
			.build();
		diaryRepository.save(expiredDiary);
		Long expiredDiaryId = expiredDiary.getId();
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/diaries/{diaryId}", expiredDiaryId)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorName", is(DIARY_EXPIRED.name())))
			.andExpect(jsonPath("$.msg", is(DIARY_EXPIRED.getErrorMsg())))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("diaryId").description("수정할 회고 아이디")
				),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("errorName").type(STRING).description("결과 코드"),
					fieldWithPath("msg").type(STRING).description("결과 메시지")
				)));
	}

}

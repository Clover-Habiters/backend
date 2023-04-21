package com.clover.habbittracker.domain.diary.api;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.entity.Diary;
import com.clover.habbittracker.domain.diary.repository.DiaryRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.exception.ErrorType;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class DiaryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private DiaryRepository diaryRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private JwtProvider jwtProvider;

	private String accessJwt;

	private Diary saveDiary;

	@BeforeEach
	void setUp() {
		Member testMember = memberRepository.save(createTestMember());
		accessJwt = jwtProvider.createAccessJwt(testMember.getId());
		Diary diary = Diary.builder()
			.endUpdateDate(LocalDateTime.now().plusHours(24))
			.member(testMember)
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
				.header("Authorization","Bearer " + accessJwt)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 회고록을 수정 할 수 있다.")
	void updateDiaryTest() throws Exception {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("회고록 수정내용 입니다.");
		String request = new ObjectMapper().writeValueAsString(diaryRequest);

		//when then
		mockMvc.perform(
				put("/diaries/"+ saveDiary.getId())
					.header("Authorization","Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(saveDiary.getId().intValue())))
			.andExpect(jsonPath("$.content", is(diaryRequest.getContent())))
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 회고록을 전체 조회 할 수 있다.")
	void getMyDiaryListTest() throws Exception {
		//when then
		mockMvc.perform(
				get("/diaries")
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 나의 회고록을 삭제 할 수 있다.")
	void deleteDiaryTest() throws Exception {
		//when then
		mockMvc.perform(
				delete("/diaries/" + saveDiary.getId())
					.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isNoContent())
			.andDo(print());
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
		//when then
		mockMvc.perform(
				put("/diaries/"+ expiredDiary.getId())
					.header("Authorization","Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errorName", is(ErrorType.DIARY_EXPIRED.name())))
			.andExpect(jsonPath("$.msg", is(ErrorType.DIARY_EXPIRED.getErrorMsg())))
			.andDo(print());
	}

}

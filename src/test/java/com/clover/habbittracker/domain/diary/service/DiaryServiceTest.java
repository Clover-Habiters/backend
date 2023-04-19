package com.clover.habbittracker.domain.diary.service;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.dto.DiaryResponse;
import com.clover.habbittracker.domain.diary.entity.Diary;
import com.clover.habbittracker.domain.diary.exception.DiaryException;
import com.clover.habbittracker.domain.diary.repository.DiaryRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;

@SpringBootTest
public class DiaryServiceTest {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private DiaryService diaryService;
	@Autowired
	private DiaryRepository diaryRepository;

	private Member testMember;

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("사용자 ID와 회고내용을 받아 회고록을 등록 할 수 있다.")
	@Transactional
	void registerTest() {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("테스트 회고록입니다.");

		//when
		Long diaryId = diaryService.register(testMember.getId(), diaryRequest);

		// then
		Optional<Diary> savedDiary = diaryRepository.findById(diaryId);
		assertThat(savedDiary).isPresent();
		assertThat(savedDiary.get().getMember()).isEqualTo(testMember);
	}

	@Test
	@DisplayName("사용자 Id와 수정 할 회고 내용을 받아 회고록을 수정 할 수 있다.")
	void updateDiaryTest() {
		//given
		DiaryRequest diaryRequest = new DiaryRequest("테스트 회고록입니다.");
		Long diaryId = diaryService.register(testMember.getId(), diaryRequest);
		DiaryRequest updateRequest = new DiaryRequest("회고록 수정내용입니다.");

		//when
		DiaryResponse diaryResponse = diaryService.updateDiary(diaryId, updateRequest);

		//then
		assertThat(diaryResponse)
			.hasFieldOrPropertyWithValue("id", diaryId)
			.hasFieldOrPropertyWithValue("content", updateRequest.getContent());
	}

	@Test
	@DisplayName("회고록은 최초 작성 후 24시간이 지나면 수정할 수 없다.")
	void endUpdateDateTest() {
		//given
		Diary diary = Diary.builder()
			.content("테스트 회고록입니다.")
			.member(testMember)
			.endUpdateDate(LocalDateTime.now().minusDays(1))
			.build();
		Diary saveDiary = diaryRepository.save(diary);
		DiaryRequest updateRequest = new DiaryRequest("회고록 수정내용입니다.");
		//when 	then
		assertThrows(DiaryException.class,() -> diaryService.updateDiary(saveDiary.getId(), updateRequest));

	}

	@Test
	@DisplayName("사용자 Id를 받아 나의 월별 회고록 리스트를 조회 할 수 있다.")
	@Transactional
	void getMyDiaryList() {
		//given
		for (int i = 0; i < 10; i++) {
			diaryService.register(testMember.getId(),new DiaryRequest("테스트 회고록입니다." + i));
		}

		//when
		List<DiaryResponse> myList1 = diaryService.getMyList(testMember.getId(), "2023-04");
		List<DiaryResponse> myList2 = diaryService.getMyList(testMember.getId(), "2023-02");

		//then
		myList1.forEach(
			diaryResponse -> assertThat(diaryResponse)
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("content"));

		assertThat(myList2.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("회고 내용이 없다면 등록 시 예외가 터진다.")
	void failedRegisterDiaryTest() {
		//given
		Long testMemberId = testMember.getId();
		DiaryRequest diaryRequest = new DiaryRequest(null);

		//when then
		assertThrows(DiaryException.class, () -> diaryService.register(testMemberId, diaryRequest));
	}

	@Test
	@DisplayName("잘못된 회고록 ID, 사용자 ID 를 요청하면 예외가 터진다.")
	void wrongHabitIdTest() {
		//given
		Long wrongDiaryId = 0L;
		DiaryRequest diaryRequest = new DiaryRequest("테스트 회고록입니다.");

		//when then
		assertAll(
			() -> assertThrows(NoSuchElementException.class, () -> diaryService.register(wrongDiaryId,diaryRequest)),
			() -> assertThrows(NoSuchElementException.class, () -> diaryService.updateDiary(wrongDiaryId,diaryRequest))
		);
	}

}

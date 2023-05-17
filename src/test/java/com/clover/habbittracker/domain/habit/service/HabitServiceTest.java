package com.clover.habbittracker.domain.habit.service;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.exception.HabitNotFoundException;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.habitcheck.exception.HabitCheckDuplicateException;
import com.clover.habbittracker.domain.habitcheck.exception.HabitCheckExpiredException;
import com.clover.habbittracker.domain.habitcheck.repository.HabitCheckRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;

@SpringBootTest
public class HabitServiceTest {

	@Autowired
	private HabitService habitService;

	@Autowired
	private HabitRepository habitRepository;

	@Autowired
	private HabitCheckRepository habitCheckRepository;
	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;

	private final LocalDateTime now = LocalDateTime.now();

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("사용자 Id와 습관정보를 받아 습관을 등록 할 수 있다.")
	void registerHabitTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");

		//when
		Long habitId = habitService.register(testMemberId, habitRequest);

		//then
		Optional<Habit> saveHabit = habitRepository.findById(habitId);
		assertThat(saveHabit).isPresent();
		assertThat(saveHabit.get().getId()).isEqualTo(habitId);
	}

	@Test
	@DisplayName("올바르지 않은 사용자 ID 또는 습관 내용이 없다면 등록 시 예외가 터진다.")
	void failedRegisterHabitTest() {
		//given
		Long testMemberId = testMember.getId();
		Long wrongMemberId = 0L;
		HabitRequest habitRequest = new HabitRequest(null);

		//when then
		assertThrows(IllegalArgumentException.class, () -> habitService.register(testMemberId, habitRequest));
		assertThrows(MemberNotFoundException.class, () -> habitService.register(wrongMemberId, habitRequest));
	}

	@Test
	@DisplayName("사용자 Id와 날짜로 나의 월별 습관 리스트를 조회 할 수 있다.")
	void getMyHabitListTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		habitService.register(testMemberId, habitRequest);
		String today = now.getYear() + "-0" + now.getMonthValue();


		//when
		List<MyHabitResponse> myList1 = habitService.getMyList(testMemberId, "2023-04"); // 2023년 4월로 조회.
		List<MyHabitResponse> myList2 = habitService.getMyList(testMemberId, today);

		//then
		myList1.forEach(myHabitResponse ->
			assertThat(myHabitResponse)
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("content")
		);


		myList2.forEach(myHabitResponse ->
			assertThat(myHabitResponse)
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("content")
		);
	}

	@Test
	@DisplayName("사용자 Id와 습관 수정 내용을 요청받아, 습관 내용을 수정 할 수 있다.")
	void updateHabitTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		HabitRequest updateHabitRequest = new HabitRequest("수정된 습관");
		Long saveHabitId = habitService.register(testMemberId, habitRequest);

		//when
		HabitResponse habitResponse = habitService.updateMyHabit(saveHabitId, updateHabitRequest);

		//then
		assertThat(habitResponse).hasFieldOrPropertyWithValue("content", updateHabitRequest.getContent());
	}

	@Test
	@DisplayName("습관 Id로 해당 습관의 수행 여부를 체크 할 수 있다.")
	@Transactional
	void habitCheckTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();

		Long saveHabitId = habitService.register(testMemberId, habitRequest);

		//when
		habitService.habitCheck(saveHabitId ,today);
		Habit saveHabit = habitRepository.findById(saveHabitId).get();

		//then
		Optional<HabitCheck> saveHabitCheck = habitCheckRepository.findByHabit(saveHabit);
		assertThat(saveHabitCheck).isPresent();
		assertThat(saveHabitCheck.get().getHabit()).isEqualTo(saveHabit);
	}

	@Test
	@DisplayName("습관 수행 여부 체크는 해당 날짜에 두번 체크 요청시 예외가 터진다.")
	void duplicateHabitCheckTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		Long saveHabitId = habitService.register(testMemberId, habitRequest);
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();

		//when
		habitService.habitCheck(saveHabitId,today);
		assertThrows(HabitCheckDuplicateException.class,
			() -> habitService.habitCheck(saveHabitId,today)); // 같은 날 두번 연속 체크는 불가능.
	}

	@Test
	@DisplayName("습관 수행 여부 체크는 오늘이 아니라면 예외가 터진다.")
	void expiredHabitCheckTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		Long saveHabitId = habitService.register(testMemberId, habitRequest);
		LocalDateTime yesterday = now.minusDays(1);
		String past = "0" + yesterday.getMonthValue() + "-" + yesterday.getDayOfMonth();

		//when
		assertThrows(HabitCheckExpiredException.class,
			() -> habitService.habitCheck(saveHabitId,past));
	}

	@Test
	@DisplayName("습관 수행 여부 체크는 다시 취소 할 수 있다.")
	void habitUnCheckTest() {
		//given
		Long testMemberId = testMember.getId();
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		Long saveHabitId = habitService.register(testMemberId, habitRequest);
		Habit saveHabit = habitRepository.findById(saveHabitId).get();
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();

		habitService.habitCheck(saveHabitId, today);

		//when
		habitService.habitUnCheck(saveHabitId);

		//then
		Optional<HabitCheck> unCheckHabit = habitCheckRepository.findByHabit(saveHabit);
		assertThat(unCheckHabit).isEmpty();
	}

	@Test
	@DisplayName("잘못된 습관 ID를 요청하면 예외가 터진다.")
	void wrongHabitIdTest() {
		//given
		Long wrongHabitId = 0L;
		HabitRequest habitRequest = new HabitRequest("테스트습관");
		String today = "0" + now.getMonthValue() + "-" + now.getDayOfMonth();

		//when then
		assertAll(
			() -> assertThrows(HabitNotFoundException.class, () -> habitService.habitCheck(wrongHabitId , today)),
			() -> assertThrows(HabitNotFoundException.class, () -> habitService.habitUnCheck(wrongHabitId)),
			() -> assertThrows(HabitNotFoundException.class,
				() -> habitService.updateMyHabit(wrongHabitId, habitRequest))
		);
	}
}

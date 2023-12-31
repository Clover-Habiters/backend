package com.clover.habbittracker.domain.habit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
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
import com.clover.habbittracker.global.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

	private final HabitRepository habitRepository;

	private final HabitCheckRepository habitCheckRepository;
	private final MemberRepository memberRepository;

	@Override
	public Long register(Long memberId, HabitRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));
		Habit habit = Habit.builder()
			.content(request.getContent())
			.member(member).build();
		habitRepository.save(habit);
		return habit.getId();
	}

	@Override
	public List<MyHabitResponse> getMyList(Long memberId, String date) {
		Map<String, LocalDateTime> dateMap = DateUtil.getMonthStartAndEndDate(date);
		return habitRepository.joinHabitCheckFindByMemberId(memberId)
			.stream()
			.map(habit -> MyHabitResponse.from(habit, dateMap))
			.toList();

	}

	@Override
	@Transactional
	public HabitResponse updateMyHabit(Long habitId, HabitRequest request) {
		Habit habit = habitRepository.findById(habitId)
			.orElseThrow(() -> new HabitNotFoundException(habitId));
		habit.setContent(request.getContent());
		return HabitResponse.from(habit);
	}

	@Override
	@Transactional
	public void habitCheck(Long habitId, String date) {
		Habit habit = habitRepository.findById(habitId)
			.orElseThrow(() -> new HabitNotFoundException(habitId));
		LocalDate requestDate = DateUtil.getLocalDate(date);
		if (!isToday(requestDate)) {
			throw new HabitCheckExpiredException(habitId);
		}

		habitCheckRepository.findByHabitOrderByUpdatedAtDesc(habit)
			.ifPresent(lastHabitCheck -> {
				if (isToday(lastHabitCheck.getUpdateDate().toLocalDate())) {
					System.out.println(lastHabitCheck.getUpdateDate());
					throw new HabitCheckDuplicateException(habitId);
				}
			});
		habitCheckRepository.save(HabitCheck.builder().checked(true).habit(habit).build());
		habit.setUpdateDate(LocalDateTime.now());
	}

	@Override
	public void deleteHabit(Long habitId) {
		habitRepository.deleteById(habitId);
	}

	@Override
	public void habitUnCheck(Long habitCheckId) {
		habitCheckRepository.deleteById(habitCheckId);
	}

	private boolean isToday(LocalDate dateTime) {
		return dateTime.isEqual(LocalDate.now());
	}
}

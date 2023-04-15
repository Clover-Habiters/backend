package com.clover.habbittracker.domain.habit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.exception.HabitException;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.habitcheck.repository.HabitCheckRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.util.DateCalculate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

	private final HabitRepository habitRepository;

	private final HabitCheckRepository habitCheckRepository;
	private final MemberRepository memberRepository;

	@Override
	public Long register(Long memberId, HabitRequest request) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("회원 정보가 존재하지 않습니다."));
		Habit habit = Habit.builder()
			.content(request.getContent())
			.member(member).build();
		habitRepository.save(habit);
		return habit.getId();
	}

	@Override
	public List<MyHabitResponse> getMyList(Long memberId, String date) {
		Map<String, LocalDateTime> dateMap = DateCalculate.startEnd(date);
		return habitRepository.joinHabitCheckFindByMemberId(memberId)
			.stream()
			.map(habit -> MyHabitResponse.from(habit, dateMap))
			.toList();

	}

	@Override
	@Transactional
	public HabitResponse updateMyHabit(Long habitId, HabitRequest request) {
		Habit habit = habitRepository.findById(habitId).orElseThrow(IllegalArgumentException::new);
		habit.setContent(request.getContent());
		return HabitResponse.from(habit);
	}

	@Override
	@Transactional
	public void habitCheck(Long habitId) {
		Habit habit = habitRepository.findById(habitId).orElseThrow(IllegalArgumentException::new);
		if (!validDate(habit.getUpdatedAt())) {
			throw new HabitException("습관체크는 오늘만 가능합니다.");
		}

		habitCheckRepository.findByHabitOrderByUpdatedAtDesc(habit)
			.ifPresent(lastHabitCheck -> {
				if (validDate(lastHabitCheck.getUpdatedAt())) {
					throw new HabitException("같은 날 두번 체크는 불가능합니다.");
				}
			});
		habitCheckRepository.save(HabitCheck.builder().checked(true).habit(habit).build());
		habit.setUpdatedAt(LocalDateTime.now());
	}

	@Override
	public void deleteHabit(Long habitId) {
		habitRepository.deleteById(habitId);
	}

	@Override
	public void HabitUnCheck(Long habitId) {
		Habit habit = habitRepository.findById(habitId).orElseThrow(IllegalArgumentException::new);
		habitCheckRepository.findByHabitOrderByUpdatedAtDesc(habit)
			.ifPresent(lastHabitCheck -> {
				if (validDate(lastHabitCheck.getUpdatedAt())) {
					throw new HabitException("같은 날 두번 체크는 불가능합니다.");
				}
			});
	}

	private boolean validDate(LocalDateTime updateDate) {
		return updateDate.toLocalDate().isEqual(LocalDate.now());
	}
}

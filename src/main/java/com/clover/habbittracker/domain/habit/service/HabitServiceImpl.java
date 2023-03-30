package com.clover.habbittracker.domain.habit.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;
import com.clover.habbittracker.domain.habit.entity.Habit;
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
		Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
		Habit habit = Habit.builder()
			.content(request.getContent())
			.member(member).build();
		habitRepository.save(habit);
		return habit.getId();
	}

	@Override
	public List<MyHabitResponse> getMyList(Long memberId,String date) {
		Map<String, LocalDateTime> dateMap = DateCalculate.startEnd(date);
		return
			habitRepository.joinHabitCheckFindByMemberId(memberId,dateMap.get("start"),dateMap.get("end"))
				.stream().map(MyHabitResponse::from)
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
		if (!vaildDate(habit.getUpdatedAt())) {
			throw new RuntimeException("에러"); // 전체 예외 적용 시 커스텀 예외로 변경 예정.
		}
		habitCheckRepository.findByHabitOrderByUpdatedAtDesc(habit)
			.ifPresent(habitCheck -> {
				if (vaildDate(habitCheck.getUpdatedAt())) {
					throw new RuntimeException("같은 체크 두번 반복"); // 전체 예외 적용 시 커스텀 예외로 변경 예정.
				}
			});
		habitCheckRepository.save(HabitCheck.builder().checked(true).habit(habit).build());
		habit.setUpdatedAt(LocalDateTime.now());
	}

	private boolean vaildDate(LocalDateTime updateDate) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(updateDate,now);
		return duration.toDays() == 0;
	}
}

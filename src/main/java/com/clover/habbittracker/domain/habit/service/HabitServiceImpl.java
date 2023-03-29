package com.clover.habbittracker.domain.habit.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;
import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

	private final HabitRepository habitRepository;

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
		Map<String, LocalDateTime> dateMap = DateCalc.startEnd(date);
		return
			habitRepository.joinHabitCheckFindByMemberId(memberId,dateMap.get("start"),dateMap.get("end"))
				.stream().map(MyHabitResponse::from)
			.toList();
	}

	@Override
	public HabitResponse updateMyHabit(Long memberId, Long habitId, HabitRequest request) {
		Habit habit = habitRepository.findById(habitId).orElseThrow(IllegalArgumentException::new);
		habit.setContent(request.getContent());
		return HabitResponse.from(habit);
	}

	private static class DateCalc {
		private static final String FORMAT = "-01T00:00:00.000000";

		private static Map<String,LocalDateTime> startEnd(String date) {
			Map<String, LocalDateTime> result = new HashMap<>();
			if (date == null) {
				date = LocalDateTime.now().toString().substring(0,7);
			}
			LocalDateTime parse = LocalDateTime.parse(date + FORMAT);
			result.put("start", parse.withDayOfMonth(1));
			result.put("end", parse.plusMonths(1));
			return result;
		}
	}
}

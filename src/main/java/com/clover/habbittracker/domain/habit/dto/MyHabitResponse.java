package com.clover.habbittracker.domain.habit.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habitcheck.dto.HabitCheckResponse;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyHabitResponse {
	private Long id;
	private String content;
	private List<HabitCheckResponse> habitChecks;
	private LocalDateTime createDate;

	public static MyHabitResponse from(Habit habit) {
		return MyHabitResponse.builder()
			.id(habit.getId())
			.content(habit.getContent())
			.createDate(habit.getCreatedAt())
			.habitChecks(HabitCheckResponse.from(habit.getHabitChecks()))
			.build();
	}
}

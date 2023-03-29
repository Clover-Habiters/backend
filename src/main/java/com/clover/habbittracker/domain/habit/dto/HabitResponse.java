package com.clover.habbittracker.domain.habit.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.habit.entity.Habit;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HabitResponse {
	private Long id;
	private String content;
	private LocalDateTime createDate;

	public static HabitResponse from(Habit habit) {
		return HabitResponse.builder()
			.id(habit.getId())
			.content(habit.getContent())
			.createDate(habit.getCreatedAt())
			.build();
	}
}

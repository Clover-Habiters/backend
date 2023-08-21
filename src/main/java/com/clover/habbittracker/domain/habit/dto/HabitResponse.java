package com.clover.habbittracker.domain.habit.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HabitResponse {
	private Long id;
	private String content;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime createDate;

	public static HabitResponse from(Habit habit) {
		return HabitResponse.builder()
			.id(habit.getId())
			.content(habit.getContent())
			.createDate(habit.getCreateDate())
			.build();
	}
}

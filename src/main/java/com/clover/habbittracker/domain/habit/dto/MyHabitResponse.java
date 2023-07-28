package com.clover.habbittracker.domain.habit.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habitcheck.dto.HabitCheckResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyHabitResponse {
	private Long id;
	private String content;
	private List<HabitCheckResponse> habitChecks;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime createDate;

	public static MyHabitResponse from(Habit habit, Map<String, LocalDateTime> dateMap) {
		return MyHabitResponse.builder()
			.id(habit.getId())
			.content(habit.getContent())
			.createDate(habit.getCreatedAt())
			.habitChecks(HabitCheckResponse.from(habit.getHabitChecks().stream()
				.filter(habitCheck -> habitCheck.getCreatedAt().isBefore(dateMap.get("end")))
				.filter(habitCheck -> habitCheck.getCreatedAt().isAfter(dateMap.get("start")))
				.toList()))
			.build();
	}
}

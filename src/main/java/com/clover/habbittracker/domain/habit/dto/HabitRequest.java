package com.clover.habbittracker.domain.habit.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class HabitRequest {
	String content;

	public String getContent() {
		return Optional.ofNullable(content).orElseThrow(IllegalArgumentException::new);
	}

}

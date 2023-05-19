package com.clover.habbittracker.domain.habitcheck.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class HabitCheckRequest {
	String requestDate;

	public String getRequestDate() {
		return Optional.ofNullable(requestDate).orElseThrow(IllegalArgumentException::new);
	}
}

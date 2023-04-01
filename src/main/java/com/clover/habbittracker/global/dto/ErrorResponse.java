package com.clover.habbittracker.global.dto;

import com.clover.habbittracker.domain.habit.exception.HabitException;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private String msg;

	public static ErrorResponse from(HabitException e) {
		return ErrorResponse.builder().msg(e.getLocalizedMessage()).build();
	}
}

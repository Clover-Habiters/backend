package com.clover.habbittracker.domain.habit.exception;

import com.clover.habbittracker.global.exception.ErrorType;

import lombok.Getter;

@Getter
public abstract class HabitException extends RuntimeException{
	private final ErrorType errorType;
	private final String msg;

	public HabitException(ErrorType errorType, String msg) {
		this.errorType = errorType;
		this.msg = msg;
	}
}

package com.clover.habbittracker.domain.habitcheck.exception;

import com.clover.habbittracker.global.exception.ErrorType;

import lombok.Getter;

@Getter
public abstract class HabitCheckException extends RuntimeException{
	private final ErrorType errorType;
	private final String msg;

	public HabitCheckException(ErrorType errorType, String msg) {
		this.errorType = errorType;
		this.msg = msg;
	}
}

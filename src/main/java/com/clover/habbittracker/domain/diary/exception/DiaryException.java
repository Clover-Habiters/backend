package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.exception.ErrorType;

import lombok.Getter;

@Getter
public abstract class DiaryException extends RuntimeException{
	private final ErrorType errorType;
	private final String msg;

	public DiaryException(ErrorType errorType, String msg) {
		this.errorType = errorType;
		this.msg = msg;
	}

}

package com.clover.habbittracker.global.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
	private final Object reason;
	private final ErrorType errorType;

	public BaseException(Object reason, ErrorType errorType) {
		this.reason = reason;
		this.errorType = errorType;
	}
}

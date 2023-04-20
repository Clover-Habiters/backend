package com.clover.habbittracker.domain.member.exception;

import com.clover.habbittracker.global.exception.ErrorType;

import lombok.Getter;

@Getter
public abstract class MemberException extends RuntimeException {
	private final ErrorType errorType;
	private final String msg;

	public MemberException(ErrorType errorType, String msg) {
		this.errorType = errorType;
		this.msg = msg;
	}
}

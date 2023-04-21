package com.clover.habbittracker.global.dto;

import com.clover.habbittracker.global.exception.ErrorType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private String errorName;
	private String msg;

	public static ErrorResponse from(ErrorType errorType) {
		return ErrorResponse.builder()
			.errorName(errorType.name())
			.msg(errorType.getErrorMsg())
			.build();
	}

}

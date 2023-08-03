package com.clover.habbittracker.global.base.dto;

import java.util.Objects;

import org.springframework.validation.BindingResult;

import com.clover.habbittracker.global.base.exception.ErrorType;

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

	public static ErrorResponse from(BindingResult bindingResult) {
		return ErrorResponse.builder()
			.errorName(Objects.requireNonNull(bindingResult.getFieldError()).getObjectName().toUpperCase())
			.msg(bindingResult.getFieldError().getDefaultMessage())
			.build();
	}

}

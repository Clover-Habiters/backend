package com.clover.habbittracker.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
	private final String code;
	private final String message;
	private final T data;

	@Builder
	private ApiResponse(ResultCode code, String message, T data) {
		this.code = code.name();
		this.message = message;
		this.data = data;
	}


	public static <T> ApiResponse<T> success() {
		return ApiResponse.<T>builder()
			.code(ResultCode.SUCCESS)
			.message(ResultCode.SUCCESS.getMessage())
			.build();
	}

	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.<T>builder()
			.code(ResultCode.SUCCESS)
			.message(ResultCode.SUCCESS.getMessage())
			.data(data)
			.build();
	}

	public static <T> ApiResponse<T> failure(ResultCode code) {
		return ApiResponse.<T>builder()
			.code(code)
			.message(code.getMessage())
			.build();
	}

}

package com.clover.habbittracker.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

	private final String code;
	private final String message;
	private final T data;

	// public BaseResponse(T data, ResponseType responseType) {
	// 	this.data = data;
	// 	this.code = responseType.getCode();
	// 	this.message = responseType.getMessage();
	// }
	//
	// public BaseResponse(ResponseType responseType) {
	// 	this.data = null;
	// 	this.code = responseType.getCode();
	// 	this.message = responseType.getMessage();
	// }

	public static <T> BaseResponse<T> of(T data,ResponseType responseType) {
		return BaseResponse.<T>builder()
			.data(data)
			.code(responseType.name())
			.message(responseType.getMessage())
			.build();
	}
}

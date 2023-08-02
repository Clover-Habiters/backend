package com.clover.habbittracker.global.restdocs.enums;

public class EnumResponse<T> {
	private T data;

	public EnumResponse() {
		this(null);
	}

	private EnumResponse(T data) {
		this.data = data;
	}

	public static <T> EnumResponse<T> of(T data) {
		return new EnumResponse<>(data);
	}

	public T getData() {
		return data;
	}
}

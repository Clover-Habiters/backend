package com.clover.habbittracker.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	INVALID_ARGUMENTS(HttpStatus.BAD_REQUEST, "내용이 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다."),

	HABIT_NOT_FOUND(HttpStatus.NOT_FOUND, "습관 정보가 존재하지 않습니다."),
	HABIT_CHECK_DUPLICATE(HttpStatus.CONFLICT, "중복 체크는 불가능합니다."),
	HABIT_CHECK_EXPIRED(HttpStatus.BAD_REQUEST, "습관체크는 오늘만 가능합니다."),

	DIARY_EXPIRED(HttpStatus.BAD_REQUEST, "회고록은 작성 후 24시간 이내만 수정 할 수 있습니다."),
	DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "회고록 정보가 존재하지 않습니다.");

	private final HttpStatus status;
	private final String errorMsg;

	ErrorType(HttpStatus status, String errorMsg) {
		this.status = status;
		this.errorMsg = errorMsg;
	}
}

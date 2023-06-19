package com.clover.habbittracker.global.dto;

import lombok.Getter;

@Getter
public enum ResponseType {
	HABIT_CREATE("습관이 정상적으로 생성 되었습니다."),
	HABIT_READ("습관 조회 요청이 성공 하였습니다."),
	HABIT_UPDATE("습관 정보가 정상적으로 업데이트 되었습니다."),
	HABIT_DELETE("습관이 정상적으로 삭제 되었습니다."),
	HABIT_CHECK_CREATE("습관 체크가 정상적으로 생성 되었습니다."),
	HABIT_CHECK_DELETE("습관 체크가 정상적으로 삭제 되었습니다."),
	MEMBER_READ("회원 조회 요청이 성공하였습니다."),
	MEMBER_UPDATE("회원 정보가 정상적으로 업데이트 되었습니다."),
	MEMBER_DELETE("회원이 정상적으로 삭제 되었습니다."),
	DIARY_CREATE("회고록이 정상적으로 생성 되었습니다."),
	DIARY_READ("회고록 조회 요청이 성공하였습니다."),
	DIARY_UPDATE("회고록 정보가 정상적으로 업데이트 되었습니다."),
	DIARY_DELETE("회고록이 정상적으로 삭제 되었습니다."),
	POST_READ("글 조회 요청이 성공하였습니다.");


	private final String message;

	ResponseType(String message) {
		this.message = message;
	}
}

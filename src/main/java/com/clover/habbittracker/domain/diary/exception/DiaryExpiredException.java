package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class DiaryExpiredException extends DiaryException{

	public DiaryExpiredException() {
		super(ErrorType.DIARY_EXPIRED,"");
	}
}

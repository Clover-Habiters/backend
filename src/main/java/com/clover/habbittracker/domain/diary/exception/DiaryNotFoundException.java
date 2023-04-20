package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class DiaryNotFoundException extends DiaryException{

	public DiaryNotFoundException() {
		super(ErrorType.DIARY_NOT_FOUND,"");
	}
}

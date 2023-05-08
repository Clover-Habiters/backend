package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class DiaryExpiredException extends BaseException {

	public DiaryExpiredException(Long diaryId) {
		super(diaryId, ErrorType.DIARY_EXPIRED);
	}
}

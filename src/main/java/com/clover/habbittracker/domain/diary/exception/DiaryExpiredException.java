package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class DiaryExpiredException extends BaseException {

	public DiaryExpiredException(Long diaryId) {
		super(diaryId, ErrorType.DIARY_EXPIRED);
	}
}

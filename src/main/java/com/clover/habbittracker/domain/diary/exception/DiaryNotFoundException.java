package com.clover.habbittracker.domain.diary.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class DiaryNotFoundException extends BaseException {

	public DiaryNotFoundException(Long diaryId) {
		super(diaryId,ErrorType.DIARY_NOT_FOUND);
	}
}

package com.clover.habbittracker.domain.habitcheck.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class HabitCheckExpiredException extends BaseException {

	public HabitCheckExpiredException(Long habitId) {
		super(habitId, ErrorType.HABIT_CHECK_EXPIRED);
	}
}

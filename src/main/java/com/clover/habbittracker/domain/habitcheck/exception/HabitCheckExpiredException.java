package com.clover.habbittracker.domain.habitcheck.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class HabitCheckExpiredException extends HabitCheckException{

	public HabitCheckExpiredException() {
		super(ErrorType.HABIT_CHECK_EXPIRED,"");
	}
}

package com.clover.habbittracker.domain.habitcheck.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class HabitCheckDuplicateException extends HabitCheckException{

	public HabitCheckDuplicateException() {
		super(ErrorType.HABIT_CHECK_DUPLICATE,"");
	}
}

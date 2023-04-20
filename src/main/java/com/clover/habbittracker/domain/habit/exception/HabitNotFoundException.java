package com.clover.habbittracker.domain.habit.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class HabitNotFoundException extends HabitException {


	public HabitNotFoundException() {
		super(ErrorType.HABIT_NOT_FOUND , "");
	}

}

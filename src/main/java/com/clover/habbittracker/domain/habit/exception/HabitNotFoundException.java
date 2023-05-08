package com.clover.habbittracker.domain.habit.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class HabitNotFoundException extends BaseException {

	public HabitNotFoundException(Long habitId) {
		super(habitId, ErrorType.HABIT_NOT_FOUND);
	}

}

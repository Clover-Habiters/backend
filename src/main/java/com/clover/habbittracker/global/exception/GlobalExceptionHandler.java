package com.clover.habbittracker.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clover.habbittracker.domain.diary.exception.DiaryException;
import com.clover.habbittracker.domain.habit.exception.HabitException;
import com.clover.habbittracker.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> serverError() {
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler({HabitException.class, DiaryException.class})
	public ResponseEntity<ErrorResponse> habitCheckError(HabitException e) {
		return new ResponseEntity<>(ErrorResponse.from(e), HttpStatus.CONFLICT);
	}
}

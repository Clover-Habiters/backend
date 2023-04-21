package com.clover.habbittracker.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clover.habbittracker.domain.diary.exception.DiaryException;
import com.clover.habbittracker.domain.habit.exception.HabitException;
import com.clover.habbittracker.domain.habitcheck.exception.HabitCheckException;
import com.clover.habbittracker.global.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final String LOG_FORM = "[{}] Cause :{} ";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> serverError(RuntimeException e) {
		ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR;
		log.error(LOG_FORM, e.getClass().getSimpleName(), errorType.getErrorMsg());
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(HabitException.class)
	public ResponseEntity<ErrorResponse> habitException(HabitException e) {
		ErrorType errorType = e.getErrorType();
		log.warn(LOG_FORM, e.getClass().getSimpleName(), errorType.getErrorMsg());
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(HabitCheckException.class)
	public ResponseEntity<ErrorResponse> habitException(HabitCheckException e) {
		ErrorType errorType = e.getErrorType();
		log.warn(LOG_FORM, e.getClass().getSimpleName(), errorType.getErrorMsg());
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(DiaryException.class)
	public ResponseEntity<ErrorResponse> diaryException(DiaryException e) {
		ErrorType errorType = e.getErrorType();
		log.warn(LOG_FORM, e.getClass().getSimpleName(), errorType.getErrorMsg());
		return new ResponseEntity<>(ErrorResponse.from(errorType), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
		ErrorType errorType = ErrorType.INVALID_ARGUMENTS;
		log.warn(LOG_FORM, e.getClass().getSimpleName(), errorType.getErrorMsg());
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}
}

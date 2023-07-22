package com.clover.habbittracker.global.exception;

import static com.clover.habbittracker.global.log.MyLogger.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clover.habbittracker.global.base.dto.ErrorResponse;
import com.clover.habbittracker.global.report.annotation.Report;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Report
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> serverError(HttpServletRequest request, RuntimeException e) {
		ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR;
		commonExceptionErrorLogging(request, e, errorType);
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> baseExceptionHandler(HttpServletRequest request, BaseException e) {
		ErrorType errorType = e.getErrorType();
		warnExceptionLogging(request, e);
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> illegalArgumentException(HttpServletRequest request,
		IllegalArgumentException e) {
		ErrorType errorType = ErrorType.INVALID_ARGUMENTS;
		commonExceptionWarnLogging(request, e, errorType);
		return new ResponseEntity<>(ErrorResponse.from(errorType), errorType.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> validationException(HttpServletRequest request,
		MethodArgumentNotValidException e) {
		ErrorType errorType = ErrorType.INVALID_ARGUMENTS;
		BindingResult bindingResult = e.getBindingResult();
		commonExceptionWarnLogging(request, e, errorType);
		return new ResponseEntity<>(ErrorResponse.from(bindingResult), errorType.getStatus());
	}
}

package com.clover.habbittracker.global.security.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class JwtStructureException extends BaseException {

	public JwtStructureException(String message) {
		super(message, ErrorType.INVALID_JWT_STRUCTURE);
	}
}

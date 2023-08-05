package com.clover.habbittracker.global.auth.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class JwtStructureException extends BaseException {

	public JwtStructureException(String message) {
		super(message, ErrorType.INVALID_JWT_STRUCTURE);
	}
}

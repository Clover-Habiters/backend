package com.clover.habbittracker.global.security.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class JwtExpiredException extends BaseException {

	public JwtExpiredException(String token) {
		super(token, ErrorType.EXPIRED_JWT);
	}
}

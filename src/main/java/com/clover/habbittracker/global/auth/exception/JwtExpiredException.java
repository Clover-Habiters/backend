package com.clover.habbittracker.global.auth.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class JwtExpiredException extends BaseException {

	public JwtExpiredException(String token) {
		super(token, ErrorType.EXPIRED_JWT);
	}
}

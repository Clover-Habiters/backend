package com.clover.habbittracker.domain.member.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class MemberNotFoundException extends BaseException {

	public MemberNotFoundException(Long memberId) {
		super(memberId, ErrorType.MEMBER_NOT_FOUND);
	}
}

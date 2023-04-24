package com.clover.habbittracker.domain.member.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class MemberNotFoundException extends BaseException {

	public MemberNotFoundException (Long memberId){
		super(memberId,ErrorType.MEMBER_NOT_FOUND);
	}
}

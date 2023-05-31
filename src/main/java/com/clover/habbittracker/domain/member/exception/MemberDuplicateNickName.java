package com.clover.habbittracker.domain.member.exception;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

public class MemberDuplicateNickName extends BaseException {

	public MemberDuplicateNickName(String nickName) {
		super(nickName, ErrorType.MEMBER_DUPLICATE_NICKNAME);
	}
}

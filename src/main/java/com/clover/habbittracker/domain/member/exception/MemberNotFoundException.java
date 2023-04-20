package com.clover.habbittracker.domain.member.exception;

import com.clover.habbittracker.global.exception.ErrorType;

public class MemberNotFoundException extends MemberException{

	public MemberNotFoundException (){
		super(ErrorType.MEMBER_NOT_FOUND,"");
	}
}

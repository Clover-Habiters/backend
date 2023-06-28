package com.clover.habbittracker.global.exception;

public class PermissionDeniedException extends BaseException {
	public PermissionDeniedException(Long memberId) {
		super(memberId,ErrorType.NO_PERMISSIONS);
	}
}

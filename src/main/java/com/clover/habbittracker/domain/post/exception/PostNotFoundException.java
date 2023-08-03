package com.clover.habbittracker.domain.post.exception;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

public class PostNotFoundException extends BaseException {

	public PostNotFoundException(Long postId) {
		super(postId, ErrorType.POST_NOT_FOUND);
	}
}

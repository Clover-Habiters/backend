package com.clover.habbittracker.domain.comment.service;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;

public interface CommentService {


	Long createComment(Long memberId, Long postId, CommentRequest request);


	void updateComment(Long memberId, Long commentId, Long postId, CommentRequest request);
}

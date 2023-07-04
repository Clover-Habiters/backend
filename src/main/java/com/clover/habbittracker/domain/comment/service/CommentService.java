package com.clover.habbittracker.domain.comment.service;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;

public interface CommentService {


	CommentResponse createComment(Long memberId, Long postId, CommentRequest request);


	CommentResponse updateComment(Long memberId, Long commentId, Long postId, CommentRequest request);

	void getReplyList(Long commentId);

	void createReply(Long commentId);
}

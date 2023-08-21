package com.clover.habbittracker.domain.comment.service;

import java.util.List;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;

public interface CommentService {

	CommentResponse createComment(Long memberId, Long postId, CommentRequest request);

	List<CommentResponse> getCommentsOf(Long postId);

	CommentResponse updateComment(Long memberId, Long commentId, Long postId, CommentRequest request);

	List<CommentResponse> getReplyList(Long commentId, Long postId);

	void createReply(Long memberId, Long commentId, Long postId, CommentRequest request);

}

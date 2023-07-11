package com.clover.habbittracker.domain.comment.api;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.service.CommentService;
import com.clover.habbittracker.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{postId}/comment")
	@ResponseStatus(CREATED)
	public ApiResponse<CommentResponse> createComment(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long postId,
		@RequestBody CommentRequest request
	) {
		CommentResponse comment = commentService.createComment(memberId, postId, request);
		return ApiResponse.success(comment);
	}

	@PutMapping("/{postId}/comment/{commentId}")
	public ApiResponse<CommentResponse> updateComment(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long commentId,
		@PathVariable Long postId,
		@RequestBody CommentRequest request
	) {
		CommentResponse updateComment = commentService.updateComment(memberId, commentId, postId, request);
		return ApiResponse.success(updateComment);
	}

	@GetMapping("/{postId}/comment/{commentId}/reply")
	public ApiResponse<List<CommentResponse>> getReplyList(
		@PathVariable Long postId,
		@PathVariable Long commentId
	) {
		List<CommentResponse> replyList = commentService.getReplyList(commentId, postId);
		return ApiResponse.success(replyList);
	}

	@PostMapping("/{postId}/comment/{commentId}/reply")
	@ResponseStatus(CREATED)
	public ApiResponse<Void> createReply(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long commentId,
		@PathVariable Long postId,
		@RequestBody CommentRequest request
	) {
		commentService.createReply(memberId, commentId, postId, request);
		return ApiResponse.success();
	}
}

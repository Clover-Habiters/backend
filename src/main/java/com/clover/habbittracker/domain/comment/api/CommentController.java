package com.clover.habbittracker.domain.comment.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{postId}/comments")
	public void createComment(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long postId,
		@RequestBody CommentRequest request
	) {
		Long commentId = commentService.createComment(memberId, postId, request);
	}

	@PutMapping("/{postId}/comments/{commentId}")
	public void updateComment(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long commentId,
		@PathVariable Long postId,
		@RequestBody CommentRequest request
	) {
		commentService.updateComment(memberId, commentId, postId, request);
	}
}

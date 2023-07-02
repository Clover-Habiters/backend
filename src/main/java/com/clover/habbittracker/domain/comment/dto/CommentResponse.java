package com.clover.habbittracker.domain.comment.dto;

import java.util.List;

import com.clover.habbittracker.domain.comment.entity.Comment;

public record CommentResponse(
	Long id,
	String content
) {
	public static List<CommentResponse> from(List<Comment> comments) {
		return comments.stream().map(comment -> new CommentResponse(comment.getId(), comment.getContent())).toList();
	}
}

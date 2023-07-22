package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.post.entity.Post;

public record PostResponse(
	Long id,
	String title,
	String content,
	Post.Category category,
	Long views,
	Integer numOfComments,
	Integer numOfEmojis,
	LocalDateTime createDate
) {
}

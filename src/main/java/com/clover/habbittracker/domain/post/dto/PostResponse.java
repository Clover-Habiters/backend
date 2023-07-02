package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.post.entity.Category;

public record PostResponse(
	Long id,
	String title,
	String content,
	Category category,
	Long views,
	Integer numOfComments,
	Integer numOfLikes,
	LocalDateTime createDate
) {
}

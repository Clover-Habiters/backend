package com.clover.habbittracker.domain.post.dto;

import com.clover.habbittracker.domain.post.entity.Category;

public record PostRequest(
	String title,
	String content,
	Category category
) {

}

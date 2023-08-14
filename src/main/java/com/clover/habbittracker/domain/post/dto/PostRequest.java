package com.clover.habbittracker.domain.post.dto;

import com.clover.habbittracker.domain.post.entity.Post;

public record PostRequest(
	String title,
	String content,
	String thumbnail,
	Post.Category category
) {

}

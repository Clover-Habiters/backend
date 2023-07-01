package com.clover.habbittracker.domain.post.dto;

import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSimpleResponse {

	private Long id;
	private String title;
	private Category category;

	public static PostSimpleResponse from(Post post) {
		return PostSimpleResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.category(post.getCategory())
			.build();
	}
}
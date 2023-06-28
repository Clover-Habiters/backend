package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {
	private Long id;
	private String title;
	private String content;
	private Category category;
	private Long views;
	private Integer numOfComment;
	private Integer numOfLikes;
	private LocalDateTime createDate;

	public static PostResponse from(Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.category(post.getCategory())
			.views(post.getViews())
			.numOfComment(post.getComments().size())
			.numOfLikes(post.getLikes().size())
			.createDate(post.getCreatedAt())
			.build();
	}
}

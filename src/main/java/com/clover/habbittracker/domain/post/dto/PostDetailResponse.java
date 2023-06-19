package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.like.entity.Like;
import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponse {
	private String title;
	private String content;
	private Category category;
	private List<Comment> comments;
	private List<Like> likes;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;

	public static PostDetailResponse from(Post post) {
		return PostDetailResponse.builder()
			.title(post.getTitle())
			.content(post.getContent())
			.category(post.getCategory())
			.comments(post.getComments())
			.likes(post.getLikes())
			.createDate(post.getCreatedAt())
			.updateDate(post.getUpdatedAt())
			.build();
	}
}

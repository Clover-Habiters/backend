package com.clover.habbittracker.domain.post.dto;

import com.clover.habbittracker.domain.post.entity.Category;

import lombok.Getter;

@Getter
public class PostRequest {
	private String title;
	private String content;
	private Category category;
}

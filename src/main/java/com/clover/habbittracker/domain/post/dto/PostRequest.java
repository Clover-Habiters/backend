package com.clover.habbittracker.domain.post.dto;

import com.clover.habbittracker.domain.post.entity.Category;

import lombok.Getter;

@Getter
public class PostRequest {
	String title;
	String content;
	Category category;
}

package com.clover.habbittracker.domain.bookmark.dto;

import java.util.List;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.post.dto.PostSimpleResponse;

public record BookmarkResponse(
	Long id,
	String title,
	String description,
	List<PostSimpleResponse> posts
) {
	public static BookmarkResponse from(Bookmark bookmark, List<PostSimpleResponse> posts) {
		return new BookmarkResponse(
			bookmark.getId(),
			bookmark.getTitle(),
			bookmark.getDescription(),
			posts
		);
	}
}

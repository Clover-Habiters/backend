package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.like.entity.Like;
import com.clover.habbittracker.domain.post.entity.Category;

public record PostDetailResponse(
	String title,
	String content,
	Category category,
	Long views,
	List<CommentResponse> comments,
	List<Like> likes,
	LocalDateTime createDate,
	LocalDateTime updateDate
) {

}

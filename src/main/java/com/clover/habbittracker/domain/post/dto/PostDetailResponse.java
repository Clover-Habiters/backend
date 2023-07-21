package com.clover.habbittracker.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.post.entity.Post;

public record PostDetailResponse(
	String title,
	String content,
	Post.Category category,
	Long views,
	List<CommentResponse> comments,
	List<Emoji> emojis,
	LocalDateTime createDate,
	LocalDateTime updateDate
) {

}

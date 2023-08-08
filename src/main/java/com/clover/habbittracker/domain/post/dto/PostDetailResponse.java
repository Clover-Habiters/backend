package com.clover.habbittracker.domain.post.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetailResponse(
	String title,
	String content,
	Post.Category category,
	Long views,
	List<CommentResponse> comments,
	List<Emoji> emojis,
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime createDate,
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime updateDate
) {

}

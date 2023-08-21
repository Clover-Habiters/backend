package com.clover.habbittracker.domain.comment.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.emoji.dto.EmojiResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CommentResponse(
	Long id,
	String content,

	List<EmojiResponse> emojis,
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	LocalDateTime createDate,
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	LocalDateTime updateDate
) {

}

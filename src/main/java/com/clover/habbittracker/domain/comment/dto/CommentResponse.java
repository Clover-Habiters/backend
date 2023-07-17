package com.clover.habbittracker.domain.comment.dto;

import java.time.LocalDateTime;

public record CommentResponse(
	Long id,
	String content,
	LocalDateTime createDate,
	LocalDateTime updateDate
) {

}

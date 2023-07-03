package com.clover.habbittracker.domain.bookmark.dto;

import jakarta.validation.constraints.NotNull;

public record CreateBookmarkRequest(

	@NotNull(message = "북마크의 제목을 지어주세요.")
	String title,

	@NotNull(message = "북마크의 설명을 적어주세요.")
	String description

) {

}

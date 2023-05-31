package com.clover.habbittracker.domain.diary.dto;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class DiaryRequest {

	@Size(max = 200, message = "회고록은 200글자 이상 작성 할 수 없습니다.")
	@NotNull(message = "회고록이 비어 있을 수 없습니다.")
	private String content;

	public String getContent() {
		return Optional.ofNullable(content).orElseThrow(IllegalArgumentException::new);
	}
}

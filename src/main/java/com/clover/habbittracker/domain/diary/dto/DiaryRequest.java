package com.clover.habbittracker.domain.diary.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequest {

	private String content;

	public String getContent() {
		return Optional.ofNullable(content).orElseThrow(IllegalArgumentException::new);
	}
}

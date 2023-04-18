package com.clover.habbittracker.domain.diary.dto;

import java.util.Optional;

import com.clover.habbittracker.domain.diary.exception.DiaryException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequest {

	private String content;

	public String getContent() {
		return Optional.ofNullable(content).orElseThrow(() -> new DiaryException("회고 내용이 없습니다."));
	}
}

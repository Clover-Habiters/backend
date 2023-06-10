package com.clover.habbittracker.domain.diary.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.diary.entity.Diary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiaryResponse {
	private Long id;
	private String content;
	private LocalDateTime createDate;
	private LocalDateTime endUpdateDate;

	public static DiaryResponse from(Diary diary) {
		return DiaryResponse.builder()
			.id(diary.getId())
			.content(diary.getContent())
			.createDate(diary.getCreatedAt())
			.endUpdateDate(diary.getEndUpdateDate())
			.build();
	}

}

package com.clover.habbittracker.domain.diray.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.diray.entity.Diary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiaryResponse {
	private Long id;
	private String content;
	private LocalDateTime createDate;

	public static DiaryResponse from(Diary diary) {
		return DiaryResponse.builder()
			.id(diary.getId())
			.content(diary.getContent())
			.createDate(diary.getUpdatedAt())
			.build();
	}

}

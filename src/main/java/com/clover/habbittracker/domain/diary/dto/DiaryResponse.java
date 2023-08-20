package com.clover.habbittracker.domain.diary.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

import java.time.LocalDateTime;

import com.clover.habbittracker.domain.diary.entity.Diary;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiaryResponse {
	private Long id;
	private String content;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime createDate;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime endUpdateDate;

	public static DiaryResponse from(Diary diary) {
		return DiaryResponse.builder()
			.id(diary.getId())
			.content(diary.getContent())
			.createDate(diary.getCreateDate())
			.endUpdateDate(diary.getEndUpdateDate())
			.build();
	}

}

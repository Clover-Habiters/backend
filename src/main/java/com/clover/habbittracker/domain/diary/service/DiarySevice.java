package com.clover.habbittracker.domain.diary.service;

import java.util.List;

import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.dto.DiaryResponse;

public interface DiarySevice {
	Long register(Long memberId, DiaryRequest request);

	List<DiaryResponse> getMyList(Long memberId, String date);

	DiaryResponse updateDiary(Long diaryId, DiaryRequest request);

	void delete(Long diaryId);
}

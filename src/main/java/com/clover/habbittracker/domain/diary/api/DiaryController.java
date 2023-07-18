package com.clover.habbittracker.domain.diary.api;

import static com.clover.habbittracker.global.base.dto.ResponseType.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.diary.dto.DiaryRequest;
import com.clover.habbittracker.domain.diary.dto.DiaryResponse;
import com.clover.habbittracker.domain.diary.service.DiaryService;
import com.clover.habbittracker.global.base.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping
	ResponseEntity<BaseResponse<Long>> createDiary(@AuthenticationPrincipal Long memberId,
		@Valid @RequestBody DiaryRequest request) {
		Long registerId = diaryService.register(memberId, request);
		BaseResponse<Long> response = BaseResponse.of(registerId, DIARY_CREATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	ResponseEntity<BaseResponse<List<DiaryResponse>>> getMyDiaryList(@AuthenticationPrincipal Long memberId,
		@RequestParam(required = false) String date) {
		List<DiaryResponse> myDiaryList = diaryService.getMyList(memberId, date);
		BaseResponse<List<DiaryResponse>> response = BaseResponse.of(myDiaryList, DIARY_READ);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/{diaryId}")
	ResponseEntity<BaseResponse<DiaryResponse>> updateDiary(@PathVariable Long diaryId,
		@Valid @RequestBody DiaryRequest request) {
		DiaryResponse updateDiary = diaryService.updateDiary(diaryId, request);
		BaseResponse<DiaryResponse> response = BaseResponse.of(updateDiary, DIARY_UPDATE);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/{diaryId}")
	ResponseEntity<BaseResponse<Void>> deleteDiary(@PathVariable Long diaryId) {
		diaryService.delete(diaryId);
		BaseResponse<Void> response = BaseResponse.of(null, HABIT_DELETE);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}
}

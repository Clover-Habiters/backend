package com.clover.habbittracker.domain.diary.api;

import static com.clover.habbittracker.global.dto.ResponseType.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.clover.habbittracker.global.dto.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping
	ResponseEntity<BaseResponse<Long>> createDiary(Authentication authentication, @RequestBody DiaryRequest request) {
		Long memberId = (Long)authentication.getPrincipal();
		Long data = diaryService.register(memberId, request);
		return new ResponseEntity<>(BaseResponse.of(data, DIARY_CREATE), HttpStatus.CREATED);
	}

	@GetMapping
	ResponseEntity<BaseResponse<List<DiaryResponse>>> getMyDiaryList(Authentication authentication,
		@RequestParam(required = false) String date) {
		Long memberId = (Long)authentication.getPrincipal();
		List<DiaryResponse> data = diaryService.getMyList(memberId, date);
		return new ResponseEntity<>(BaseResponse.of(data, DIARY_READ), HttpStatus.OK);
	}

	@PutMapping("/{diaryId}")
	ResponseEntity<BaseResponse<DiaryResponse>> updateDiary(@PathVariable Long diaryId,
		@RequestBody DiaryRequest request) {
		DiaryResponse data = diaryService.updateDiary(diaryId, request);
		return new ResponseEntity<>(BaseResponse.of(data, DIARY_UPDATE), HttpStatus.OK);
	}

	@DeleteMapping("/{diaryId}")
	ResponseEntity<BaseResponse<Void>> deleteDiary(@PathVariable Long diaryId) {
		diaryService.delete(diaryId);
		return new ResponseEntity<>(BaseResponse.of(null, DIARY_DELETE), HttpStatus.NO_CONTENT);
	}
}

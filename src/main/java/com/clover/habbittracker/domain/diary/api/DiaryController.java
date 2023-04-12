package com.clover.habbittracker.domain.diary.api;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping
	ResponseEntity<Long> createDiary(Authentication authentication,@RequestBody DiaryRequest request) {
		Long memberId = (Long)authentication.getPrincipal();
		return new ResponseEntity<>(diaryService.register(memberId,request), HttpStatus.OK);
	}

	@GetMapping
	ResponseEntity<List<DiaryResponse>> getMyDiaryList(Authentication authentication, @RequestParam(required = false) String date) {
		Long memberId = (Long) authentication.getPrincipal();
		return new ResponseEntity<>(diaryService.getMyList(memberId,date),HttpStatus.OK);
	}

	@PutMapping("/{diaryId}")
	ResponseEntity<DiaryResponse> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryRequest request) {
		return new ResponseEntity<>(diaryService.updateDiary(diaryId, request),HttpStatus.OK);
	}

	@DeleteMapping("/{diaryId}")
	ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
		diaryService.delete(diaryId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

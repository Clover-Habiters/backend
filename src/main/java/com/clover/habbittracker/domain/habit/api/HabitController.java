package com.clover.habbittracker.domain.habit.api;

import static com.clover.habbittracker.global.dto.ResponseType.*;

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

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;
import com.clover.habbittracker.domain.habit.service.HabitService;
import com.clover.habbittracker.global.dto.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("habits")
@RequiredArgsConstructor
public class HabitController {

	private final HabitService habitService;

	@GetMapping
	public ResponseEntity<BaseResponse<List<MyHabitResponse>>> getMyHabitList(
			@AuthenticationPrincipal Long memberId,
			@RequestParam(required = false) String date) {
		List<MyHabitResponse> myHabitList = habitService.getMyList(memberId, date);
		BaseResponse<List<MyHabitResponse>> response = BaseResponse.of(myHabitList, HABIT_READ);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	public ResponseEntity<BaseResponse<Long>> createHabit(
			@AuthenticationPrincipal Long memberId,
			@RequestBody HabitRequest request) {
		Long registerId = habitService.register(memberId, request);
		BaseResponse<Long> response = BaseResponse.of(registerId, HABIT_CREATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("{habitId}")
	public ResponseEntity<BaseResponse<HabitResponse>> updateHabit(
			@PathVariable Long habitId,
			@RequestBody HabitRequest request) {
		HabitResponse updateHabit = habitService.updateMyHabit(habitId, request);
		BaseResponse<HabitResponse> response = BaseResponse.of(updateHabit, HABIT_UPDATE);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("{habitId}")
	public ResponseEntity<BaseResponse<Void>> deleteHabit(@PathVariable Long habitId) {
		habitService.deleteHabit(habitId);
		BaseResponse<Void> response = BaseResponse.of(null, HABIT_DELETE);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

	@PostMapping("{habitId}/check")
	public ResponseEntity<BaseResponse<Void>> HabitCheck(@PathVariable Long habitId) {
		habitService.habitCheck(habitId);
		BaseResponse<Void> response = BaseResponse.of(null, HABIT_CHECK_CREATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("{habitId}/check")
	public ResponseEntity<BaseResponse<Void>> HabitUnCheck(@PathVariable Long habitId) {
		habitService.habitUnCheck(habitId);
		BaseResponse<Void> response = BaseResponse.of(null, HABIT_CHECK_DELETE);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

}

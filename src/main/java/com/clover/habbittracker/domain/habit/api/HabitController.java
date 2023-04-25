package com.clover.habbittracker.domain.habit.api;

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
	ResponseEntity<BaseResponse<List<MyHabitResponse>>> getMyHabitList(Authentication authentication,
		@RequestParam(required = false) String date) {
		Long memberId = (Long)authentication.getPrincipal();
		List<MyHabitResponse> data = habitService.getMyList(memberId, date);
		return new ResponseEntity<>(BaseResponse.of(data, HABIT_READ), HttpStatus.OK);
	}

	@PostMapping
	ResponseEntity<BaseResponse<Long>> createHabit(
		Authentication authentication,
		@RequestBody HabitRequest request) {

		Long memberId = (Long)authentication.getPrincipal();
		Long data = habitService.register(memberId, request);
		return new ResponseEntity<>(BaseResponse.of(data, HABIT_CREATE), HttpStatus.CREATED);
	}

	@PutMapping("{habitId}")
	ResponseEntity<BaseResponse<HabitResponse>> updateHabit(@PathVariable Long habitId,
		@RequestBody HabitRequest request) {
		HabitResponse data = habitService.updateMyHabit(habitId, request);
		return new ResponseEntity<>(BaseResponse.of(data, HABIT_UPDATE), HttpStatus.OK);
	}

	@DeleteMapping("{habitId}")
	ResponseEntity<BaseResponse<Void>> deleteHabit(@PathVariable Long habitId) {
		habitService.deleteHabit(habitId);
		return new ResponseEntity<>(BaseResponse.of(null, HABIT_DELETE), HttpStatus.NO_CONTENT);
	}

	@PostMapping("{habitId}/check")
	ResponseEntity<BaseResponse<Void>> HabitCheck(@PathVariable Long habitId) {
		habitService.habitCheck(habitId);
		return new ResponseEntity<>(BaseResponse.of(null, HABIT_CHECK_CREATE), HttpStatus.CREATED);
	}

	@DeleteMapping("{habitId}/check")
	ResponseEntity<BaseResponse<Void>> HabitUnCheck(@PathVariable Long habitId) {
		habitService.habitUnCheck(habitId);
		return new ResponseEntity<>(BaseResponse.of(null, HABIT_CHECK_DELETE), HttpStatus.NO_CONTENT);
	}
}

package com.clover.habbittracker.domain.habit.service;

import java.util.List;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;
import com.clover.habbittracker.domain.habit.dto.HabitResponse;
import com.clover.habbittracker.domain.habit.dto.MyHabitResponse;

public interface HabitService {
	Long register(Long MemberId, HabitRequest request);

	List<MyHabitResponse> getMyList(Long memberId,String date);

	HabitResponse updateMyHabit(Long habitId, HabitRequest request);

	void habitCheck(Long habitId);
}

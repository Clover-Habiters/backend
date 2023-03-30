package com.clover.habbittracker.domain.habitcheck.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HabitCheckResponse {
	private Long id;
	private LocalDateTime updatedAt;

	public static List<HabitCheckResponse> from(List<HabitCheck> habitChecks) {
		return habitChecks.stream()
			.map(habitCheck -> new HabitCheckResponse(
				habitCheck.getId(),
				habitCheck.getUpdatedAt()
			)).toList();
	}
}

package com.clover.habbittracker.domain.habitcheck.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

import java.time.LocalDateTime;
import java.util.List;

import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HabitCheckResponse {
	private Long id;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime updatedAt;

	public static List<HabitCheckResponse> from(List<HabitCheck> habitChecks) {
		return habitChecks.stream()
			.map(habitCheck -> new HabitCheckResponse(
				habitCheck.getId(),
				habitCheck.getUpdateDate()
			)).toList();
	}
}

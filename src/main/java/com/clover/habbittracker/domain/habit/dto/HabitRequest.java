package com.clover.habbittracker.domain.habit.dto;

import java.util.Optional;

import com.clover.habbittracker.domain.habit.exception.HabitException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class HabitRequest {
	String content;

	public String getContent() {
		return Optional.ofNullable(content).orElseThrow(()-> new HabitException("내용을 작성해주세요."));
	}

}

package com.clover.habbittracker.domain.habit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitRequest {

	@Size(max = 10, message = "습관은 10글자 이내로 입력해주세요.")
	@NotNull(message = "습관 내용이 비어 있을 수 없습니다.")
	String content;
}

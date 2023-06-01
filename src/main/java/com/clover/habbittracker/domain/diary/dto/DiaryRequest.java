package com.clover.habbittracker.domain.diary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DiaryRequest {

	@Size(max = 200, message = "회고록은 200글자 이내로 작성해주세요.")
	@NotNull(message = "회고록이 비어 있을 수 없습니다.")
	private String content;

}

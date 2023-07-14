package com.clover.habbittracker.domain.emoji.dto;

import com.clover.habbittracker.domain.emoji.entity.Type;

import jakarta.validation.constraints.NotNull;

public record EmojiRequest(
	@NotNull(message = "emoji의 type은 null이 될 수 없습니다.")
	Type type
) {

}

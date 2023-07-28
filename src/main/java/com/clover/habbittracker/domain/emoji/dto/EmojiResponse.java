package com.clover.habbittracker.domain.emoji.dto;

public record EmojiResponse(
	String emojiType,
	Long memberId,
	String domain,
	Long domainId
) {
}

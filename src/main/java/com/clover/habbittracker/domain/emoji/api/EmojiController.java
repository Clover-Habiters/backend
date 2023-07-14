package com.clover.habbittracker.domain.emoji.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.service.EmojiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}")
public class EmojiController {

	private final EmojiService emojiService;

	@PostMapping("/emojis")
	public void clickEmojiOnPost(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long postId,
		@RequestParam Emoji.Type type
	) {
		emojiService.clickOnPost(memberId, postId, type);
	}

	@PostMapping("/comments/{commentId}/emojis")
	public void clickEmojiOnComment(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long commentId,
		@RequestParam Emoji.Type type
	) {
		emojiService.clickOnComment(memberId, commentId, type);
	}

	@GetMapping("/emojis")
	public int getAmountEmojisInPost(
		@PathVariable Long postId
	) {
		return emojiService.getAmountInPost(postId);
	}

}
package com.clover.habbittracker.domain.emoji.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.dto.EmojiRequest;
import com.clover.habbittracker.domain.emoji.service.EmojiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emoji")
public class EmojiController {

	private final EmojiService emojiService;

	@PostMapping
	public void updateEmojiStatus(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody EmojiRequest request
	) {
		emojiService.updateStatus(userId, request);
	}

}
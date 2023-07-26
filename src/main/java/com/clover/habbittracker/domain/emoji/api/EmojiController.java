package com.clover.habbittracker.domain.emoji.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.dto.EmojiResponse;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.service.EmojiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{domain}/{domainId}/emojis")
public class EmojiController {

	private final EmojiService emojiService;

	@GetMapping
	public ResponseEntity<List<EmojiResponse>> getAllEmojis(
		@PathVariable Emoji.Domain domain,
		@PathVariable Long domainId
	) {
		List<EmojiResponse> emojis = emojiService.getAllEmojisInDomain(domain, domainId);

		return ResponseEntity.ok().body(emojis);
	}

	@PutMapping
	public ResponseEntity<EmojiResponse> saveEmoji(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Emoji.Domain domain,
		@PathVariable Long domainId,
		@RequestParam(name = "type") Emoji.Type emojiType
	) {
		EmojiResponse emoji = emojiService.save(emojiType, memberId, domain, domainId);

		return ResponseEntity.ok().body(emoji);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEmoji( // soft delete
		@AuthenticationPrincipal Long memberId,
		@PathVariable Emoji.Domain domain,
		@PathVariable Long domainId
	) {
		emojiService.delete(memberId, domain, domainId);
	}

}
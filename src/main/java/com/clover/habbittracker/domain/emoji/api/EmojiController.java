package com.clover.habbittracker.domain.emoji.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.emoji.service.EmojiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emoji")
public class EmojiController {

	private final EmojiService emojiService;

}


package com.clover.habbittracker.domain.emoji.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmojiService {

	private final EmojiRepository emojiRepository;
}

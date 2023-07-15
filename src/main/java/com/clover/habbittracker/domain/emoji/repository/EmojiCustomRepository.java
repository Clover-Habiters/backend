package com.clover.habbittracker.domain.emoji.repository;

public interface EmojiCustomRepository {
	int countByPostId(Long postId);
}

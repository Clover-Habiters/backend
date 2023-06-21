package com.clover.habbittracker.domain.emoji.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.emoji.entity.Emoji;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {

}


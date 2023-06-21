package com.clover.habbittracker.domain.emoji.repository;

import java.util.Optional;

import com.clover.habbittracker.domain.emoji.entity.Emoji;

public interface EmojiCustomRepository {

	Optional<Emoji> findByMemberIdAndCommentId(Long memberId, Long commentId);

	Optional<Emoji> findByMemberIdAndPostId(Long memberId, Long postId);
}

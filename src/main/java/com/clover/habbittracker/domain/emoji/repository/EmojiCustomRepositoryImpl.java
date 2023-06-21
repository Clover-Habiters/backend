package com.clover.habbittracker.domain.emoji.repository;

import static com.clover.habbittracker.domain.emoji.entity.QEmoji.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmojiCustomRepositoryImpl implements EmojiCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Emoji> findByMemberIdAndCommentId(Long memberId, Long commentId) {

		Emoji result = jpaQueryFactory.selectFrom(emoji)
			.where(emoji.member.id.eq(memberId).and(emoji.comment.id.eq(commentId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Emoji> findByMemberIdAndPostId(Long memberId, Long postId) {

		Emoji result = jpaQueryFactory.selectFrom(emoji)
			.where(emoji.member.id.eq(memberId).and(emoji.post.id.eq(postId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}
}

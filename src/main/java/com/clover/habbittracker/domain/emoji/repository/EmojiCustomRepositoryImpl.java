package com.clover.habbittracker.domain.emoji.repository;

import static com.clover.habbittracker.domain.emoji.entity.QEmoji.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmojiCustomRepositoryImpl implements EmojiCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public int countByPostId(Long postId) {
		return jpaQueryFactory.selectFrom(emoji)
			.where(emoji.domainId.eq(postId))
			.fetch().size();
	}
}

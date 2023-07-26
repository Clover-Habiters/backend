package com.clover.habbittracker.domain.emoji.repository;

import static com.clover.habbittracker.domain.emoji.entity.QEmoji.*;

import java.util.List;
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
	public List<Emoji> findAllInDomain(Emoji.Domain domain, Long domainId) {
		return jpaQueryFactory
			.selectFrom(emoji)
			.where(emoji.domain.eq(domain)
				.and(emoji.domainId.eq(domainId)))
			.fetch();
	}

	@Override
	public Optional<Emoji> findByUniqueKey(Long memberId, Emoji.Domain domain, Long domainId) {

		Emoji result = jpaQueryFactory
			.selectFrom(emoji)
			.where(
				emoji.memberId.eq(memberId)
					.and(emoji.domain.eq(domain))
					.and(emoji.domainId.eq(domainId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public int countByDomain(Emoji.Domain domain, Long domainId) {
		return jpaQueryFactory
			.selectFrom(emoji)
			.where(emoji.domain.eq(domain)
				.and(emoji.domainId.eq(domainId)))
			.fetch().size();
	}
}

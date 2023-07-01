package com.clover.habbittracker.domain.bookmark.repository;

import static com.clover.habbittracker.domain.bookmark.entity.QBookmark.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Bookmark> findByMemberId(Long memberId) {

		return jpaQueryFactory.selectFrom(bookmark)
			.where(bookmark.member.id.eq(memberId))
			.orderBy(bookmark.id.desc())
			.fetch();
	}

	@Override
	public Optional<Bookmark> findByIdAndMemberId(Long bookmarkId, Long memberId) {

		Bookmark result = jpaQueryFactory.selectFrom(bookmark)
			.where(bookmark.id.eq(bookmarkId).and(bookmark.member.id.eq(memberId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public void deleteByIdAndMemberId(Long bookmarkId, Long memberId) {

		jpaQueryFactory.delete(bookmark)
			.where(bookmark.id.eq(bookmarkId).and(bookmark.member.id.eq(memberId)));
	}
}

package com.clover.habbittracker.domain.bookmark.repository;

import static com.clover.habbittracker.domain.bookmark.entity.QBookMark.*;

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

		return jpaQueryFactory.selectFrom(bookMark)
			.where(bookMark.member.id.eq(memberId))
			.fetch();
	}

	@Override
	public Optional<Bookmark> findByIdAndMemberId(Long bookmarkId, Long memberId) {

		Bookmark result = jpaQueryFactory.selectFrom(bookMark)
			.where(bookMark.id.eq(bookmarkId).and(bookMark.member.id.eq(memberId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public void deleteByIdAndMemberId(Long bookmarkId, Long memberId) {

		jpaQueryFactory.delete(bookMark)
			.where(bookMark.id.eq(bookmarkId).and(bookMark.member.id.eq(memberId)));
	}
}

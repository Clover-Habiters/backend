package com.clover.habbittracker.domain.post.repository;

import static com.clover.habbittracker.domain.comment.entity.QComment.*;
import static com.clover.habbittracker.domain.member.entity.QMember.*;
import static com.clover.habbittracker.domain.post.entity.QPost.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Post> findAllPostsSummary(Pageable pageable, Category category) {

		List<Post> content = jpaQueryFactory.selectFrom(post)
			.leftJoin(post.member, member).fetchJoin()
			.where(eqCategory(category))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.createdAt.desc())
			.fetch();

		Long count = getCount();

		return new PageImpl<>(content, pageable, count);
	}

	@Override
	public Optional<Post> joinCommentAndLikeFindById(Long postId) {
		Post result = jpaQueryFactory.selectFrom(post)
			.leftJoin(post.member, member)
			.fetchJoin()
			.leftJoin(post.comments, comment)
			.fetchJoin()
			.where(eqId(postId))
			.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	public Long updateViews(Long postId) {
		return jpaQueryFactory.update(post)
			.set(post.views, post.views.add(1))
			.where(eqId(postId))
			.execute();
	}

	private BooleanExpression eqId(Long postId) {
		if (postId != null) {
			return post.id.eq(postId);
		}
		return null;
	}

	private BooleanExpression eqCategory(Category category) {
		if (category != null) {
			return post.category.eq(category);
		}
		return null;
	}

	private Long getCount() {
		return jpaQueryFactory.select(post.count())
			.from(post)
			.fetchOne();
	}
}

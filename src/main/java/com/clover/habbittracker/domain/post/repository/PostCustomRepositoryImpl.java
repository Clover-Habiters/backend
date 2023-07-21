package com.clover.habbittracker.domain.post.repository;

import static com.clover.habbittracker.domain.comment.entity.QComment.*;
import static com.clover.habbittracker.domain.member.entity.QMember.*;
import static com.clover.habbittracker.domain.post.dto.PostSearchCondition.SearchType.*;
import static com.clover.habbittracker.domain.post.entity.QPost.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Post> findAllPostsSummary(Pageable pageable, Post.Category category) {

		return jpaQueryFactory.selectFrom(post)
			.leftJoin(post.member, member).fetchJoin()
			.where(eqCategory(category))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.createdAt.desc())
			.fetch();
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

	@Override
	public Page<Post> searchPostBy(PostSearchCondition postSearchCondition, Pageable pageable) {

		List<Post> content = jpaQueryFactory.selectFrom(post)
			.leftJoin(post.member, member).fetchJoin()
			.where(
				eqFilter(postSearchCondition.getCategory()),
				matchKeyword(postSearchCondition.getSearchType(), postSearchCondition.getKeyword())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.createdAt.desc())
			.distinct()
			.fetch();

		Integer count = getCount();

		return new PageImpl<>(content, pageable, count);
	}

	private BooleanExpression eqId(Long postId) {
		if (postId != null) {
			return post.id.eq(postId);
		}
		return null;
	}

	private BooleanExpression eqCategory(Post.Category category) {
		if (category != null) {
			return post.category.eq(category);
		}
		return null;
	}

	private BooleanExpression eqFilter(Post.Category category) {
		if (category == null || Post.Category.ALL.equals(category)) {
			return null;
		}
		return post.category.eq(category);
	}

	private Integer getCount() {
		return jpaQueryFactory.select(post.count())
			.from(post)
			.fetch().size();
	}

	private BooleanExpression matchKeyword(PostSearchCondition.SearchType searchType, String keyword) {
		if (isNotValid(keyword)) {
			return null;
		} else if (searchType == TITLE) {
			NumberExpression<Double> booleanTemplate = Expressions.numberTemplate(Double.class,
				"function('match',{0},{1})", post.title, keyword);
			return booleanTemplate.gt(0);
		} else if (searchType == CONTENT) {
			NumberTemplate<Double> booleanTemplate = Expressions.numberTemplate(Double.class,
				"function('match',{0},{1})", post.content, keyword);
			return booleanTemplate.gt(0);
		} else { // TODO: 하나로 합쳐서 검색 / 나눠서 검색해서 합치기?
			NumberExpression<Double> booleanTemplate = Expressions.numberTemplate(Double.class,
				"function('match',{0},{1})", post.title, keyword);

			NumberExpression<Double> booleanTemplate2 = Expressions.numberTemplate(Double.class,
				"function('match',{0},{1})", post.content, keyword);

			BooleanExpression result = booleanTemplate.gt(0).or(booleanTemplate2.gt(0));

			return result;
		}
	}

	private boolean isNotValid(String keyword) {
		return keyword == null || keyword.isBlank() || keyword.length() < 2;
	}
}

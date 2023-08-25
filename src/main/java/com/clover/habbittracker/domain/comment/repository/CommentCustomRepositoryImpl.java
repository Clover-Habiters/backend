package com.clover.habbittracker.domain.comment.repository;

import static com.clover.habbittracker.domain.comment.entity.QComment.*;
import static com.clover.habbittracker.domain.member.entity.QMember.*;
import static com.clover.habbittracker.domain.post.entity.QPost.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Comment> findByIdAndPostId(Long commentId, Long postId) {

		Comment result = jpaQueryFactory
			.selectFrom(comment)
			.leftJoin(comment.post, post)
			.fetchJoin()
			.leftJoin(comment.member, member)
			.fetchJoin()
			.where(comment.id.eq(commentId).and(comment.post.id.eq(postId)))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<Comment> findByPostId(Long postId) {
		return jpaQueryFactory
			.selectFrom(comment)
			.leftJoin(comment.member, member)
			.fetchJoin()
			.where(eqPost(postId).and(NotReply()))
			.fetch();
	}

	private BooleanExpression NotReply() {
		return comment.parentId.isNull();
	}

	private BooleanExpression eqPost(Long postId) {
		return comment.post.id.eq(postId);
	}

	@Override
	public List<Comment> findChildCommentById(Long commentId) {

		return jpaQueryFactory.selectFrom(comment)
			.where(comment.parentId.eq(commentId))
			.fetch();
	}

}

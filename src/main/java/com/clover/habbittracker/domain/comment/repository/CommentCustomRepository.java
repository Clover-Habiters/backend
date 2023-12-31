package com.clover.habbittracker.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import com.clover.habbittracker.domain.comment.entity.Comment;

public interface CommentCustomRepository {

	Optional<Comment> findByIdAndPostId(Long commentId, Long postId);

	List<Comment> findChildCommentById(Long commentId);

	List<Comment> findByPostId(Long postId);
}

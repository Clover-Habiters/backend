package com.clover.habbittracker.domain.comment.repository;

import java.util.Optional;

import com.clover.habbittracker.domain.comment.entity.Comment;

public interface CommentCustomRepository {

	Optional<Comment> findByIdAndPostId(Long commentId, Long postId);


}

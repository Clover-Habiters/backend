package com.clover.habbittracker.domain.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;

public interface PostCustomRepository {
	Page<Post> findAllPostsSummary(Pageable pageable, Category category);

	Optional<Post> joinCommentAndLikeFindById(@Param("postId") Long postId);

	Long updateViews(Long postId);
}

package com.clover.habbittracker.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;

public interface PostCustomRepository {
	List<Post> findAllPostsSummary(Pageable pageable, Post.Category category);

	Optional<Post> joinMemberAndCommentFindById(@Param("postId") Long postId);

	Long updateViews(Long postId);

	Page<Post> searchPostBy(PostSearchCondition postSearchCondition, Pageable pageable);
}

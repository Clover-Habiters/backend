package com.clover.habbittracker.domain.post.repository;

import java.util.List;
import java.util.Optional;

import com.clover.habbittracker.domain.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;

public interface PostCustomRepository {
	List<PostResponse> findAllPostsSummary(Pageable pageable, Post.Category category);

	Optional<Post> joinMemberAndEmojisFindById(@Param("postId") Long postId);

	Long updateViews(Long postId);

	Page<PostResponse> searchPostBy(PostSearchCondition postSearchCondition, Pageable pageable);
}

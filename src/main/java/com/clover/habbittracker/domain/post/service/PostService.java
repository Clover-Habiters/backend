package com.clover.habbittracker.domain.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;

public interface PostService {
	Long register(Long memberId, PostRequest request);

	PostDetailResponse getPostBy(Long postId);

	List<PostResponse> getPostAllBy(Post.Category category, Pageable pageable);

	Page<PostResponse> getPostBy(PostSearchCondition postSearchCondition, Pageable pageable);

	Long updatePost(Long postId, PostRequest request, Long memberId);

	void deletePost(Long postId, Long memberId);
}
package com.clover.habbittracker.domain.post.api;

import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.dto.PostSearchCondition;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.service.PostService;
import com.clover.habbittracker.global.base.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createPost(
		@AuthenticationPrincipal Long memberId,
		@RequestBody PostRequest request
	) {
		Long postId = postService.register(memberId, request);
		URI location = URI.create("/posts/" + postId.toString());
		ApiResponse<Void> response = ApiResponse.success();
		return ResponseEntity.created(location).body(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PostResponse>>> getPostList(
		@PageableDefault(size = 15) Pageable pageable,
		@RequestParam(required = false) Post.Category category
	) {
		List<PostResponse> postList = postService.getPostAllBy(category, pageable);
		ApiResponse<List<PostResponse>> response = ApiResponse.success(postList);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(
		@PathVariable Long postId
	) {
		PostDetailResponse postDetail = postService.getPostBy(postId);
		ApiResponse<PostDetailResponse> response = ApiResponse.success(postDetail);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Page<PostResponse>>> searchPost(
		@PageableDefault(size = 15) Pageable pageable,
		@Valid @RequestBody PostSearchCondition postSearchCondition
	) {
		Page<PostResponse> postPages = postService.getPostBy(postSearchCondition, pageable);
		ApiResponse<Page<PostResponse>> response = ApiResponse.success(postPages);

		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostResponse>> updatePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal Long memberId,
		@RequestBody PostRequest request
	) {
		PostResponse postResponse = postService.updatePost(postId, request, memberId);
		ApiResponse<PostResponse> response = ApiResponse.success(postResponse);
		URI location = URI.create("/posts/" + postId.toString());
		return ResponseEntity.ok().location(location).body(response);
	}

	@DeleteMapping("/{postId}")
	@ResponseStatus(NO_CONTENT)
	public ApiResponse<Void> deletePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal Long memberId
	) {
		postService.deletePost(postId, memberId);
		return ApiResponse.success();
	}
}

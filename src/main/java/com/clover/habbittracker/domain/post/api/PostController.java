package com.clover.habbittracker.domain.post.api;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.service.PostService;
import com.clover.habbittracker.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createPost(@AuthenticationPrincipal Long memberId,
		@RequestBody PostRequest request) {
		Long postId = postService.register(memberId, request);
		return ResponseEntity.created(URI.create("/posts/" + postId.toString())).body(ApiResponse.success());
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PostResponse>>> getPostList(
		@PageableDefault(size = 15) Pageable pageable,
		@RequestParam(required = false) Category category
	) {
		List<PostResponse> postList = postService.getPostList(pageable, category);
		return ResponseEntity.ok().body(ApiResponse.success(postList));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long postId) {
		PostDetailResponse postDetail = postService.getPost(postId);
		return ResponseEntity.ok().body(ApiResponse.success(postDetail));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostResponse>> updatePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal Long memberId,
		@RequestBody PostRequest request) {
		PostResponse postResponse = postService.updatePost(postId, request, memberId);
		return ResponseEntity.created(URI.create("/posts/" + postId.toString())).body(ApiResponse.success(postResponse));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal Long memberId) {
		postService.deletePost(postId, memberId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success());
	}

}

package com.clover.habbittracker.domain.post.api;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.service.PostService;
import com.clover.habbittracker.global.dto.BaseResponse;
import com.clover.habbittracker.global.dto.ResponseType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<BaseResponse<Void>> createPost(@AuthenticationPrincipal Long memberId, @RequestBody PostRequest request) {
		Long postId = postService.register(memberId, request);
		BaseResponse<Void> response = BaseResponse.of(null, ResponseType.POST_CREATE);
		return ResponseEntity.created(URI.create("/posts/" + postId.toString())).body(response);
	}
	@GetMapping
	public ResponseEntity<BaseResponse<List<PostResponse>>> getPostList(@PageableDefault(size = 15) Pageable pageable) {
		List<PostResponse> postList = postService.getPostList(pageable);
		BaseResponse<List<PostResponse>> response = BaseResponse.of(postList, ResponseType.POST_READ);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<BaseResponse<PostDetailResponse>> getPost(@PathVariable Long postId){
		PostDetailResponse postDetail = postService.getPost(postId);
		BaseResponse<PostDetailResponse> response = BaseResponse.of(postDetail, ResponseType.POST_READ);
		return ResponseEntity.ok().body(response);
	}


	@PutMapping("/{postId}")
	public ResponseEntity<BaseResponse<PostResponse>> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
		PostResponse postResponse = postService.updatePost(postId, request);
		BaseResponse<PostResponse> response = BaseResponse.of(postResponse, ResponseType.POST_UPDATE);
		return ResponseEntity.created(URI.create("/posts/" + postId.toString())).body(response);
	}

}

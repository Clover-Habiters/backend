package com.clover.habbittracker.domain.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.entity.Category;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.exception.PostNotFoundException;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.exception.PermissionDeniedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final MemberRepository memberRepository;

	@Transactional
	public Long register(Long memberId, PostRequest request) {
		Member member = getMemberBy(memberId);
		Post post = Post.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.category(request.getCategory())
			.member(member)
			.build();
		Post savedPost = postRepository.save(post);

		return savedPost.getId();
	}

	@Override
	@Transactional
	public PostDetailResponse getPost(Long postId) {
		Post post = postRepository.joinCommentAndLikeFindById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
		postRepository.updateViews(post.getId());
		return PostDetailResponse.from(post);

	}

	@Override
	public List<PostResponse> getPostList(Pageable pageable, Category category) {
		Page<Post> postsSummary = postRepository.findAllPostsSummary(pageable, category);
		return postsSummary.stream().map(PostResponse::from).toList();
	}

	@Override
	@Transactional
	public PostResponse updatePost(Long postId, PostRequest request, Long memberId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		verifyPermissions(post.getMember(), memberId);
		post.updatePost(request);
		return PostResponse.from(post);
	}

	@Override
	@Transactional
	public void deletePost(Long postId, Long memberId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		verifyPermissions(post.getMember(), memberId);
		postRepository.deleteById(postId);
	}

	private void verifyPermissions(Member member, Long memberId) {
		if (!member.getId().equals(memberId)) {
			throw new PermissionDeniedException(memberId);
		}
	}

	private Member getMemberBy(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}

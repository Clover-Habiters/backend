package com.clover.habbittracker.domain.post.service;

import java.util.List;
import java.util.Objects;

import com.clover.habbittracker.domain.post.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.exception.PostNotFoundException;
import com.clover.habbittracker.domain.post.mapper.PostMapper;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.base.exception.PermissionDeniedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final MemberRepository memberRepository;

	private final PostMapper postMapper;

	@Transactional
	public Long register(Long memberId, PostRequest request) {
		Member member = getMemberBy(memberId);
		Post post = postMapper.toPost(request, member);
		Post savedPost = postRepository.save(post);

		return savedPost.getId();
	}

	@Override
	@Transactional
	public PostDetailResponse getPostBy(Long postId) {
		Post post = postRepository.joinMemberAndEmojisFindById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
		postRepository.updateViews(post.getId());
		return postMapper.toPostDetail(post);

	}

	@Override
	public Page<PostResponse> getPostBy(PostSearchCondition postSearchCondition, Pageable pageable) {
		return postRepository.searchPostBy(postSearchCondition, pageable);
	}

	@Override
	public List<PostResponse> getPostAllBy(Post.Category category, Pageable pageable) {
		return postRepository.findAllPostsSummary(pageable, category);
	}

	@Override
	@Transactional
	public Long updatePost(Long postId, PostRequest request, Long memberId) {
		Post post = postRepository.joinMemberAndEmojisFindById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
		verifyPermissions(post.getMember(), memberId);
		post.updatePost(request);
		return post.getId();
	}

	@Override
	@Transactional
	public void deletePost(Long postId, Long memberId) {
		Post post = postRepository.joinMemberAndEmojisFindById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
		verifyPermissions(post.getMember(), memberId);
		postRepository.deleteById(postId);
	}

	private void verifyPermissions(Member member, Long memberId) {
		if (!Objects.equals(member.getId(), memberId)) {
			throw new PermissionDeniedException(memberId);
		}
	}

	private Member getMemberBy(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}

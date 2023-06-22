package com.clover.habbittracker.domain.post.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.dto.PostDetailResponse;
import com.clover.habbittracker.domain.post.dto.PostResponse;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final MemberRepository memberRepository;

	public Long register(Long memberId, PostRequest request) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
		Post post = Post.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.category(request.getCategory())
			.member(member)
			.build();
		postRepository.save(post);

		return post.getId();
	}

	@Override
	@Transactional
	public PostDetailResponse getPost(Long postId) {
		Post post = postRepository.joinCommentAndLikeFindById(postId).orElseThrow(NoSuchElementException::new); //TODO : 임시 예외 처리
		postRepository.updateViews(postId);
		return PostDetailResponse.from(post);

	}

	@Override
	public List<PostResponse> getPostList(Pageable pageable) {
		Page<Post> postsSummary = postRepository.findAllPostsSummary(pageable);
		return postsSummary.stream().map(PostResponse::from).toList();
	}

	@Override
	@Transactional
	public PostResponse updatePost(Long postId, PostRequest request) {
		Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);//TODO : 임시 예외 처리
		post.updatePost(request);
		return PostResponse.from(post);
	}
}

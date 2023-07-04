package com.clover.habbittracker.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.exception.PostNotFoundException;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.exception.PermissionDeniedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@Override
	@Transactional
	public CommentResponse createComment(Long memberId, Long postId, CommentRequest request) {
		Member member = getMemberBy(memberId);
		Post post = getPostBy(postId);
		Comment comment = Comment.builder()
			.member(member)
			.post(post)
			.content(request.content())
			.build();
		Comment saveComment = commentRepository.save(comment);

		return CommentResponse.from(saveComment);
	}


	@Override
	@Transactional
	public CommentResponse updateComment(Long memberId, Long commentId, Long postId, CommentRequest request) {
		Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
			.orElseThrow(() -> new IllegalArgumentException("NotFoundComment"));
		verifyPermissions(comment.getMember(), memberId);
		comment.updateComment(request);
		return CommentResponse.from(comment);
	}

	@Override
	public void getReplyList(Long commentId) {
		commentRepository.findChildCommentById(commentId);
	}

	@Override
	public void createReply(Long commentId) {

	}

	private void verifyPermissions(Member member, Long memberId) {
		if (!member.getId().equals(memberId)) {
			throw new PermissionDeniedException(memberId);
		}
	}

	private Post getPostBy(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
	}

	private Member getMemberBy(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}

package com.clover.habbittracker.domain.comment.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.mapper.CommentMapper;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.exception.PostNotFoundException;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.base.exception.PermissionDeniedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentMapper commentMapper;

	@Override
	@Transactional
	public CommentResponse createComment(Long memberId, Long postId, CommentRequest request) {
		Member member = getMemberBy(memberId);
		Post post = getPostBy(postId);
		Comment comment = commentMapper.toComment(request, member, post);
		Comment saveComment = commentRepository.save(comment);

		return commentMapper.toCommentResponse(saveComment);
	}

	@Override
	public List<CommentResponse> getCommentsOf(Long postId) {
		List<Comment> commentList = commentRepository.findByPostId(postId);
		return commentList.stream().map(commentMapper::toCommentResponse).toList();
	}

	@Override
	public void deleteComment(Long memberId, Long commentId, Long postId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("NotFoundComment"));
		verifyPermissions(comment.getMember(), memberId);
		commentRepository.delete(comment);
	}

	@Override
	@Transactional
	public CommentResponse updateComment(Long memberId, Long commentId, Long postId, CommentRequest request) {
		Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
			.orElseThrow(() -> new IllegalArgumentException("NotFoundComment"));
		verifyPermissions(comment.getMember(), memberId);
		comment.updateComment(request);
		return commentMapper.toCommentResponse(comment);
	}

	@Override
	public List<CommentResponse> getReplyList(Long commentId, Long postId) {
		List<Comment> childCommentList = commentRepository.findChildCommentById(commentId);
		return childCommentList.stream()
			.map(commentMapper::toCommentResponse)
			.toList();
	}

	@Override
	@Transactional
	public void createReply(Long memberId, Long commentId, Long postId, CommentRequest request) {
		Member member = getMemberBy(memberId);
		Post post = getPostBy(postId);
		Comment reply = commentMapper.toReply(request, member, post, commentId);
		commentRepository.save(reply);
	}

	private void verifyPermissions(Member member, Long memberId) {
		if (!Objects.equals(member.getId(), memberId)) {
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

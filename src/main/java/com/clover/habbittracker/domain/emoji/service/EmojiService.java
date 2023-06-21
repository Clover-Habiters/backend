package com.clover.habbittracker.domain.emoji.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.emoji.dto.EmojiRequest;
import com.clover.habbittracker.domain.emoji.entity.Domain;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.entity.Type;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmojiService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final EmojiRepository emojiRepository;

	public void updateStatus(Long memberId, EmojiRequest emojiRequest) {

		Domain domain = emojiRequest.domain();
		Type type = emojiRequest.type();

		if (domain.isPost()) {
			Long postId = emojiRequest.id();
			Optional<Emoji> emoji = emojiRepository.findByMemberIdAndPostId(memberId, postId);
			emoji.ifPresentOrElse(
				e -> updateEmojiStatus(e, type),
				() -> {
					Emoji newEmoji = createPostEmoji(memberId, type, postId);
					save(newEmoji);
				}
			);
		}

		if (domain.isComment()) {
			Long commentId = emojiRequest.id();
			Optional<Emoji> emoji = emojiRepository.findByMemberIdAndCommentId(memberId, commentId);
			emoji.ifPresentOrElse(
				e -> updateEmojiStatus(e, type),
				() -> {
					Emoji newEmoji = createCommentEmoji(memberId, type, commentId);
					save(newEmoji);
				}
			);
		}

	}

	@Transactional
	public void updateEmojiStatus(Emoji emoji, Type type) {
		emoji.updateStatus(type);
	}

	@Transactional
	public void save(Emoji emoji) {
		emojiRepository.save(emoji);
	}

	public Emoji createCommentEmoji(Long memberId, Type type, Long commentId) {
		Member member = getMember(memberId);
		Comment comment = getComment(commentId);

		return Emoji.builder()
			.type(type)
			.member(member)
			.comment(comment)
			.build();
	}

	public Emoji createPostEmoji(Long memberId, Type type, Long postId) {
		Member member = getMember(memberId);
		Post post = getPost(postId);

		return Emoji.builder()
			.type(type)
			.member(member)
			.post(post)
			.build();
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}

	private Post getPost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
	}

	private Comment getComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
	}
}

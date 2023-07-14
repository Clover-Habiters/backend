package com.clover.habbittracker.domain.emoji.service;

import static com.clover.habbittracker.domain.emoji.entity.Emoji.Domain.*;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmojiService {

	private final EmojiRepository emojiRepository;

	// 검증 로직을 제외해봤음!
	@Transactional
	public void clickOnPost(Long memberId, Long postId, Emoji.Type type) {

		Optional<Emoji> emoji = emojiRepository.findByMemberIdAndPostId(memberId, postId);

		emoji.ifPresentOrElse(
			updateOrDelete(type),
			() -> {

				Emoji newEmoji = Emoji.builder()
					.type(type)
					.domain(Emoji.Domain.POST)
					.domainId(postId)
					.memberId(memberId)
					.build();

				emojiRepository.save(newEmoji);
			}
		);
	}

	@Transactional
	public void clickOnComment(Long memberId, Long commentId, Emoji.Type type) {

		Optional<Emoji> emoji = emojiRepository.findByMemberIdAndCommentId(memberId, commentId);

		emoji.ifPresentOrElse(
			updateOrDelete(type),
			() -> {
				Emoji newEmoji = Emoji.builder()
					.type(type)
					.domain(COMMENT)
					.domainId(commentId)
					.memberId(memberId)
					.build();

				emojiRepository.save(newEmoji);
			}
		);
	}

	public int getAmountInPost(Long postId) {
		return emojiRepository.countByPostId(postId);
	}

	private Consumer<Emoji> updateOrDelete(Emoji.Type type) {
		return e -> {
			if (e.isSameType(type)) {
				e.updateStatus(Emoji.Type.NONE); // softdelete
				return;
			}
			e.updateStatus(type);
		};
	}
}

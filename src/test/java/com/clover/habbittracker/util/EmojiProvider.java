package com.clover.habbittracker.util;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.emoji.entity.Emoji;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

public class EmojiProvider {

	private EmojiProvider() {
		/* NO-OP */
	}

	public static Emoji createTestEmojiInPost(Member savedMember, Post savedPost) {
		return Emoji.builder()
			.type(Emoji.Type.SMILE)
			.memberId(savedMember.getId())
			.domain(Emoji.Domain.POST)
			.domainId(savedPost.getId())
			.build();
	}

	public static Emoji createTestEmojiInComment(Member savedMember, Comment savedComment) {
		return Emoji.builder()
			.type(Emoji.Type.SMILE)
			.memberId(savedMember.getId())
			.domain(Emoji.Domain.COMMENT)
			.domainId(savedComment.getId())
			.build();
	}
}

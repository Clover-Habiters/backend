package com.clover.habbittracker.util;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

public class CommentProvider {

	private static final String COMMENT_CONTENT = "testContent";

	private CommentProvider() {
		/* NO-OP */
	}

	public static Comment createTestComment(Member savedMember, Post savedPost) {
		return Comment.builder()
			.content(COMMENT_CONTENT)
			.member(savedMember)
			.post(savedPost)
			.build();
	}
}

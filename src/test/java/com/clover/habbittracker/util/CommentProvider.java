package com.clover.habbittracker.util;

import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

public class CommentProvider {

	private static final String COMMENT_CONTENT = "testContent";

	private static final String REPLY_CONTENT = "testReply";

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

	public static Comment createTestReply(Member savedMember, Post savedPost, Comment savedComment) {
		return Comment.builder()
			.content(REPLY_CONTENT)
			.member(savedMember)
			.post(savedPost)
			.parentId(savedComment.getId())
			.build();
	}
}

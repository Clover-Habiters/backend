package com.clover.habbittracker.global.util;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

public class PostProvider {

	private static final String TITLE = "testTitle";
	private static final String CONTENT = "testContent";
	private static final Post.Category CATEGORY = Post.Category.DAILY;

	public static Post createTestPost(Member member) {
		return Post.builder()
			.title(TITLE)
			.content(CONTENT)
			.category(CATEGORY)
			.member(member)
			.build();
	}
}

package com.clover.habbittracker.util;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.entity.Post;

public class PostProvider {

	private static final String TITLE = "testTitle";
	private static final String CONTENT = "testContent";
	private static final String REQUEST_TITLE = "requestTitle";
	private static final String REQUEST_CONTENT = "requestContent";
	private static final Post.Category CATEGORY = Post.Category.DAILY;

	private PostProvider() {
		/* NO-OP */
	}

	public static Post createTestPost(Member member) {
		return Post.builder()
			.title(TITLE)
			.content(CONTENT)
			.category(CATEGORY)
			.member(member)
			.build();
	}

	public static Post createTestPost(Member member, Post.Category category) {
		return Post.builder()
			.title(TITLE)
			.content(CONTENT)
			.category(category)
			.member(member)
			.build();
	}

	public static PostRequest createPostRequest() {
		return new PostRequest(REQUEST_TITLE, REQUEST_CONTENT, CATEGORY);
	}
}

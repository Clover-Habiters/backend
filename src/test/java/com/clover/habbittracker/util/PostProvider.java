package com.clover.habbittracker.util;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.dto.PostRequest;
import com.clover.habbittracker.domain.post.entity.Post;

public class PostProvider {

	private static final String DEFAULT_TITLE = "testTitle";
	private static final String DEFAULT_CONTENT = "testContent";
	private static final String REQUEST_TITLE = "requestTitle";
	private static final String REQUEST_CONTENT = "requestContent";
	private static final String DEFAULT_THUMBNAIL = "/thumbnail";
	private static final Post.Category DEFAULT_CATEGORY = Post.Category.DAILY;

	private PostProvider() {
		/* NO-OP */
	}

	public static Post createTestPost(Member member) {
		return Post.builder()
			.title(DEFAULT_TITLE)
			.content(DEFAULT_CONTENT)
			.category(DEFAULT_CATEGORY)
			.member(member)
			.build();
	}

	public static Post createTestPost(Member member, Post.Category category) {
		return Post.builder()
			.title(DEFAULT_TITLE)
			.content(DEFAULT_CONTENT)
			.category(category)
			.member(member)
			.build();
	}

	public static Post createTestPost(Member member, String title) {
		return Post.builder()
			.title(title)
			.content(DEFAULT_CONTENT)
			.category(DEFAULT_CATEGORY)
			.member(member)
			.build();
	}

	public static Post createTestPost(Member member, String title, String content) {
		return Post.builder()
			.title(title)
			.content(content)
			.category(DEFAULT_CATEGORY)
			.member(member)
			.build();
	}

	public static PostRequest createPostRequest() {
		return new PostRequest(REQUEST_TITLE, REQUEST_CONTENT, DEFAULT_THUMBNAIL, DEFAULT_CATEGORY);
	}
}

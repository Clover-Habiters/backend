package com.clover.habbittracker.domain.comment.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;

class CommentTest {

	private final String CONTENT = "테스트 내용";

	@Mock
	private Member member;

	@Mock
	private Post post;

	public CommentTest() {
		openMocks(this);
	}

	@Test
	@DisplayName("댓글 생성 테스트")
	void createCommentTest() {
		Comment comment = Comment.builder()
			.content(CONTENT)
			.member(member)
			.post(post)
			.build();

		assertThat(comment).hasFieldOrPropertyWithValue("content", comment.getContent());

	}

	@Test
	@DisplayName("댓글 내용을 업데이트 할 수 있다.")
	void updateCommentTest() {
		String updateContent = "update";
		Comment comment = Comment.builder()
			.content(CONTENT)
			.member(member)
			.post(post)
			.build();

		assertDoesNotThrow(() ->
			comment.updateComment(new CommentRequest(updateContent))
		);
		assertThat(comment.getContent()).isEqualTo(updateContent);
	}

	@DisplayName("공백 또는 Null 은 예외가 발생한다.")
	@ParameterizedTest(name = "Empty Or Null")
	@NullAndEmptySource
	void validateContentTest(String inputString) {
		Comment comment = Comment.builder()
			.content(CONTENT)
			.member(member)
			.post(post)
			.build();

		assertThrows(
			IllegalArgumentException.class,
			() -> comment.updateComment(new CommentRequest(inputString))
		);
	}

}

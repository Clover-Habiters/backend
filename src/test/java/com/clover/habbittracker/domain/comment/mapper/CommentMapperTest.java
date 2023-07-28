package com.clover.habbittracker.domain.comment.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.dto.CommentResponse;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.util.MemberProvider;
import com.clover.habbittracker.util.PostProvider;

public class CommentMapperTest {

	private final CommentMapper commentMapper = new CommentMapperImpl();

	private Member testMember;

	private Post testPost;

	private CommentRequest commentRequest;

	@BeforeEach
	void setUp() {
		testMember = MemberProvider.createTestMember();

		testPost = PostProvider.createTestPost(testMember);

		commentRequest = new CommentRequest("testContent");
	}

	@Test
	@DisplayName("'댓글 내용', '회원', '게시글'의 내용으로 Comment 를 생성 할 수 있다.")
	void toCommentTest() {
		//when
		Comment toComment = commentMapper.toComment(commentRequest, testMember, testPost);

		//then
		assertAll(() -> {
			assertThat(toComment.getContent()).isEqualTo(commentRequest.content());
			assertThat(toComment.getMember()).isEqualTo(testMember);
			assertThat(toComment.getPost()).isEqualTo(testPost);
		});
	}

	@Test
	@DisplayName("'댓글 내용', '회원', '게시글', '부모댓글 ID' 로 내용으로 replyComment 를 생성 할 수 있다.")
	void toReplyTest() {
		//given
		Comment comment = commentMapper.toComment(commentRequest, testMember, testPost);

		//when
		Comment reply = commentMapper.toReply(commentRequest, testMember, testPost, comment.getId());

		//then
		assertAll(() -> {
			assertThat(reply.getContent()).isEqualTo(commentRequest.content());
			assertThat(reply.getMember()).isEqualTo(testMember);
			assertThat(reply.getPost()).isEqualTo(testPost);
			assertThat(reply.getParentId()).isEqualTo(comment.getId());
		});
	}

	@Test
	@DisplayName("'댓글' 객체로 '댓글 DTO' 를 생성 할 수 있다")
	void toCommentResponseTest() {
		//given
		Comment comment = commentMapper.toComment(commentRequest, testMember, testPost);

		//when
		CommentResponse commentResponse = commentMapper.toCommentResponse(comment);

		//then
		assertAll(() ->
			assertThat(commentResponse.content()).isEqualTo(comment.getContent())
		);
	}

}

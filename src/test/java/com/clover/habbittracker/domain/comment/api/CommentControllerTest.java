package com.clover.habbittracker.domain.comment.api;

import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static com.clover.habbittracker.util.CommentProvider.*;
import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

class CommentControllerTest extends RestDocsSupport {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	private Post savePost;

	@BeforeEach
	void setUp() {
		Member author = memberRepository.save(createTestMember());
		savePost = postRepository.save(createTestPost(author));
	}

	@Test
	@DisplayName("사용자는 게시글의 댓글을 등록 할 수 있다.")
	void createCommentTest() throws Exception {
		//given
		CommentRequest commentRequest = new CommentRequest("testComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.post("/posts/{postId}/comments", savePost.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id")
				),
				requestFields(
					fieldWithPath("content").type(STRING)
						.description("댓글 내용")
						.attributes(field("constraints", "아직 미정"))
				),
				responseFields(
					beneathPath("data").withSubsectionId("data"),
					fieldWithPath("id").type(NUMBER).description("생성된 댓글 아이디"),
					fieldWithPath("content").type(STRING).description("생성된 댓글 내용"),
					fieldWithPath("authorId").type(NUMBER).description("댓글 작성자"),
					fieldWithPath("emojis[]").type(ARRAY).description("댓글에 대한 이모지"),
					fieldWithPath("createDate").type(STRING).description("댓글 생성 날짜"),
					fieldWithPath("updateDate").type(STRING).description("댓글 최근 수정 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 게시글의 댓글을 조회 할 수 있다.")
	void getCommentListTest() throws Exception {
		//given
		Comment comment = createTestComment(savedMember, savePost);
		Comment savedComment = commentRepository.save(comment);

		//when then
		mockMvc.perform(RestDocumentationRequestBuilders
				.get("/posts/{postId}/comments", savePost.getId())
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id")
				),
				responseFields(
					beneathPath("data").withSubsectionId("data"),
					fieldWithPath("id").type(NUMBER).description("댓글 아이디"),
					fieldWithPath("content").type(STRING).description("댓글 내용"),
					fieldWithPath("authorId").type(NUMBER).description("댓글 작성자"),
					fieldWithPath("emojis[]").type(ARRAY).description("댓글의 이모지"),
					fieldWithPath("createDate").type(STRING).description("댓글 생성 날짜"),
					fieldWithPath("updateDate").type(STRING).description("댓글 최근 수정 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 게시글의 댓글을 삭제 할 수 있다.")
	void deleteCommentTest() throws Exception {
		//given
		Comment comment = createTestComment(savedMember, savePost);
		Comment savedComment = commentRepository.save(comment);

		//when then
		mockMvc.perform(RestDocumentationRequestBuilders
				.delete("/posts/{postId}/comments/{commentId}", savePost.getId(), savedComment.getId())
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("삭제할 댓글 id")
				)));
	}

	@Test
	@DisplayName("사용자는 작성한 댓글을 수정 할 수 있다.")
	void updateCommentTest() throws Exception {
		//given
		Comment comment = createTestComment(savedMember, savePost);
		Comment savedComment = commentRepository.save(comment);
		CommentRequest commentRequest = new CommentRequest("updateComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.put("/posts/{postId}/comments/{commentId}", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("수정할 댓글 id")
				),
				requestFields(
					fieldWithPath("content").type(STRING)
						.description("수정할 댓글 내용")
						.attributes(field("constraints", "아직 미정"))
				),
				responseFields(
					beneathPath("data").withSubsectionId("data"),
					fieldWithPath("id").type(NUMBER).description("수정한 댓글 아이디"),
					fieldWithPath("content").type(STRING).description("수정된 댓글 내용"),
					fieldWithPath("authorId").type(NUMBER).description("댓글 작성자"),
					fieldWithPath("emojis[]").type(ARRAY).description("댓글의 이모지"),
					fieldWithPath("createDate").type(STRING).description("댓글 생성 날짜"),
					fieldWithPath("updateDate").type(STRING).description("댓글 최근 수정 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 댓글의 답글을 작성 할 수 있다.")
	void createReplyTest() throws Exception {
		//given
		Comment comment = createTestComment(savedMember, savePost);
		Comment savedComment = commentRepository.save(comment);
		CommentRequest commentRequest = new CommentRequest("testReply");
		String request = new ObjectMapper().writeValueAsString(commentRequest);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.post("/posts/{postId}/comments/{commentId}/reply", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("답글을 작성 할 댓글 id")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)));
	}

	@Test
	@DisplayName("댓글의 달린 답글을 조회 할 수 있다.")
	void getReplyListTest() throws Exception {
		//given
		Comment comment = createTestComment(savedMember, savePost);
		Comment savedComment = commentRepository.save(comment);
		Comment reply = createTestReply(savedMember, savePost, savedComment);
		commentRepository.save(reply);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/posts/{postId}/comments/{commentId}/reply", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("답글을 조회 할 댓글 id")
				),
				responseFields(
					beneathPath("data").withSubsectionId("data"),
					fieldWithPath("id").type(NUMBER).description("답글 아이디"),
					fieldWithPath("content").type(STRING).description("답글 내용"),
					fieldWithPath("authorId").type(NUMBER).description("답글 작성자"),
					fieldWithPath("emojis[]").type(ARRAY).description("댓글에 대한 이모지"),
					fieldWithPath("createDate").type(STRING).description("답글 등록 날짜"),
					fieldWithPath("updateDate").type(STRING).description("답글 수정 날짜")
				)));
	}

}

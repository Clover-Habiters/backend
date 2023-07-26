package com.clover.habbittracker.domain.comment.api;

import static com.clover.habbittracker.util.ApiDocumentUtils.*;
import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.comment.dto.CommentRequest;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.habiters.store")
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private JwtProvider jwtProvider;

	private Member commenter;

	private String accessJwt;

	private Post savePost;

	@BeforeEach
	void setUp() {
		Member author = memberRepository.save(createTestMember());
		savePost = postRepository.save(createTestPost(author));

		commenter = memberRepository.save(createTestMember());
		accessJwt = jwtProvider.createAccessJwt(commenter.getId());

	}

	@Test
	@DisplayName("사용자는 게시글의 댓글을 등록 할 수 있다.")
	void createDiaryTest() throws Exception {
		//given
		CommentRequest commentRequest = new CommentRequest("testComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.post("/posts/{postId}/comment", savePost.getId())
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			// restDocs 설정.
			.andDo(document("comment-create",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id")
				),
				requestFields(
					fieldWithPath("content").type(STRING).description("댓글 내용")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("생성된 댓글 아이디"),
					fieldWithPath("data.content").type(STRING).description("생성된 댓글 내용"),
					fieldWithPath("data.createDate").type(STRING).description("댓글 생성 날짜"),
					fieldWithPath("data.updateDate").type(STRING).description("댓글 최근 수정 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 작성한 댓글을 수정 할 수 있다.")
	void updateDiaryTest() throws Exception {
		//given
		CommentRequest commentRequest = new CommentRequest("updateComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);
		Comment comment = Comment.builder()
			.member(commenter)
			.content("testComment")
			.post(savePost)
			.build();
		Comment savedComment = commentRepository.save(comment);
		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.put("/posts/{postId}/comment/{commentId}", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andDo(document("comment-update",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("수정할 댓글 id")
				),
				requestFields(
					fieldWithPath("content").type(STRING).description("수정할 댓글 내용")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("생성된 댓글 아이디"),
					fieldWithPath("data.content").type(STRING).description("생성된 댓글 내용"),
					fieldWithPath("data.createDate").type(STRING).description("댓글 생성 날짜"),
					fieldWithPath("data.updateDate").type(STRING).description("댓글 최근 수정 날짜")
				)));
	}

	@Test
	@DisplayName("사용자는 댓글의 답글을 작성 할 수 있다.")
	void getMyDiaryListTest() throws Exception {
		//given
		CommentRequest commentRequest = new CommentRequest("updateComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);
		Comment comment = Comment.builder()
			.member(commenter)
			.content("testComment")
			.post(savePost)
			.build();
		Comment savedComment = commentRepository.save(comment);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.post("/posts/{postId}/comment/{commentId}/reply", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isCreated())
			.andDo(document("reply-create",
				getDocumentRequest(),
				getDocumentResponse(),
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
	void getAMonthlyMyDiaryListTest() throws Exception {
		//given
		CommentRequest commentRequest = new CommentRequest("updateComment");
		String request = new ObjectMapper().writeValueAsString(commentRequest);
		Comment comment = Comment.builder()
			.member(commenter)
			.content("testComment")
			.post(savePost)
			.build();
		Comment savedComment = commentRepository.save(comment);

		Comment reply = Comment.builder()
			.content("testReply")
			.member(commenter)
			.post(savePost)
			.parentId(savedComment.getId())
			.build();
		commentRepository.save(reply);

		//when then
		mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/posts/{postId}/comment/{commentId}/reply", savePost.getId(), savedComment.getId())
					.header("Authorization", "Bearer " + accessJwt)
					.contentType(APPLICATION_JSON)
					.content(request))
			.andExpect(status().isOk())
			.andDo(document("reply-read",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("게시글 id"),
					parameterWithName("commentId").description("답글을 조회 할 댓글 id")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data[].id").type(NUMBER).description("회고록 아이디"),
					fieldWithPath("data[].content").type(STRING).description("회고록 내용"),
					fieldWithPath("data[].createDate").type(STRING).description("회고 등록 날짜"),
					fieldWithPath("data[].updateDate").type(STRING).description("회고 수정 마감 날짜")
				)));
	}

}

package com.clover.habbittracker.domain.emoji.api;

import static com.clover.habbittracker.domain.emoji.entity.Emoji.*;
import static com.clover.habbittracker.util.ApiDocumentUtils.*;
import static com.clover.habbittracker.util.CommentProvider.*;
import static com.clover.habbittracker.util.EmojiProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.clover.habbittracker.base.ControllerTest;
import com.clover.habbittracker.domain.comment.entity.Comment;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.emoji.repository.EmojiRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

@DisplayName("Emoji API 테스트")
class EmojiControllerTest extends ControllerTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private EmojiRepository emojiRepository;

	private Post savedPostExistEmoji;

	private Post savedPostNotExistEmoji;

	private Comment savedCommentExistEmoji;

	private Comment savedCommentNotExistEmoji;

	@BeforeEach
	void setUpDomain() {
		// Post
		savedPostExistEmoji = postRepository.save(createTestPost(savedMember));
		emojiRepository.save(createTestEmojiInPost(savedMember, savedPostExistEmoji));
		savedPostNotExistEmoji = postRepository.save(createTestPost(savedMember));
		// Comment
		savedCommentExistEmoji = commentRepository.save(createTestComment(savedMember, savedPostExistEmoji));
		emojiRepository.save(createTestEmojiInComment(savedMember, savedCommentExistEmoji));
		savedCommentNotExistEmoji = commentRepository.save(createTestComment(savedMember, savedPostNotExistEmoji));
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[200 성공] 이모지 저장(생성)")
	void saveEmojiIn(Domain domain) throws Exception {
		//given
		Long domainId = getNotSavedEmojiDomainId(domain);
		Type type = Type.SMILE;
		//when then
		mockMvc.perform(
				put("/{domain}/{domainId}/emojis", domain.name().toLowerCase(), domainId)
					.header("Authorization", "Bearer " + accessToken)
					.queryParam("type", type.name())
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.emojiType").value(type.name()))
			.andExpect(jsonPath("$.memberId").value(savedMember.getId()))
			.andExpect(jsonPath("$.domain").value(domain.name()))
			.andExpect(jsonPath("$.domainId").value(domainId))
			.andDo(print())
			.andDo(document("emoji save(Insert or Update)",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("domain").description("이모지를 남기려는 도메인(ex. 게시글, 댓글)"),
					parameterWithName("domainId").description("이모지를 남기려는 도메인의 아이디")
				),
				queryParameters(
					parameterWithName("type").description("이모지 타입(ex. SMILE, SAD..)") // TODO: Enum 타입 문서화 추가
				),
				responseFields(
					fieldWithPath("emojiType").type(STRING).description("이모지 타입"),
					fieldWithPath("memberId").type(NUMBER).description("이모지를 남긴 회원 아이디"),
					fieldWithPath("domain").type(STRING).description("이모지를 남긴 도메인"),
					fieldWithPath("domainId").type(NUMBER).description("이모지를 남긴 도메인의 아이디")
				))
			);
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[200 성공] 이모지 저장(수정)")
	void saveForUpdateEmojiIn(Domain domain) throws Exception {
		//given
		Long domainId = getDomainId(domain);
		Type type = Type.ANGRY;
		//when then
		mockMvc.perform(
				put("/{domain}/{domainId}/emojis", domain.name().toLowerCase(), domainId)
					.header("Authorization", "Bearer " + accessToken)
					.queryParam("type", type.name())
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			// .andExpect(jsonPath("$.emojiType").value(type.name()))
			// .andExpect(jsonPath("$.memberId").value(savedMember.getId()))
			// .andExpect(jsonPath("$.domain").value(domain.name()))
			// .andExpect(jsonPath("$.domainId").value(domainId.longValue()))
			.andDo(print())
			.andDo(document("emoji save(Insert or Update)",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("domain").description("이모지를 남기려는 도메인(ex. 게시글, 댓글)"),
					parameterWithName("domainId").description("이모지를 남기려는 도메인의 아이디")
				),
				queryParameters(
					parameterWithName("type").description("이모지 타입(ex. SMILE, SAD..)")
				),
				responseFields(
					fieldWithPath("emojiType").type(STRING).description("이모지 타입"),
					fieldWithPath("memberId").type(NUMBER).description("이모지를 남긴 회원 아이디"),
					fieldWithPath("domain").type(STRING).description("이모지를 남긴 도메인"),
					fieldWithPath("domainId").type(NUMBER).description("이모지를 남긴 도메인의 아이디")
				))
			);
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[200 성공] 도메인에 속한 모든 이모지 조회")
	void getAllEmojis(Domain domain) throws Exception {
		//given
		Long domainId = getDomainId(domain);

		//when then
		mockMvc.perform(
				get("/{domain}/{domainId}/emojis", domain.name().toLowerCase(), domainId)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("emoji get all",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("domain").description("이모지를 찾고 싶은 도메인"),
					parameterWithName("domainId").description("이모지를 찾고 싶은 도메인의 아이디")
				),
				responseFields(
					fieldWithPath("[].emojiType").type(STRING).description("이모지 타입"),
					fieldWithPath("[].memberId").type(NUMBER).description("이모지를 남긴 회원 아이디"),
					fieldWithPath("[].domain").type(STRING).description("이모지를 남긴 도메인"),
					fieldWithPath("[].domainId").type(NUMBER).description("이모지를 남긴 도메인의 아이디")
				))
			);
	}

	@ParameterizedTest
	@EnumSource(Domain.class)
	@DisplayName("[204 성공] 이모지 삭제")
	void deleteEmoji(Domain domain) throws Exception {
		//given
		Long domainId = getDomainId(domain);

		//when then
		mockMvc.perform(
				delete("/{domain}/{domainId}/emojis", domain.name().toLowerCase(), domainId)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(document("emoji delete",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("domain").description("이모지를 삭제하려는 도메인"),
					parameterWithName("domainId").description("이모지를 삭제하려는 도메인의 아이디")
				))
			);
	}

	private Long getDomainId(Domain domain) {
		if (domain == Domain.POST) {
			return savedPostExistEmoji.getId();
		}
		if (domain == Domain.COMMENT) {
			return savedCommentExistEmoji.getId();
		}
		return null;
	}

	private Long getNotSavedEmojiDomainId(Domain domain) {
		if (domain == Domain.POST) {
			return savedPostNotExistEmoji.getId();
		}
		if (domain == Domain.COMMENT) {
			return savedCommentNotExistEmoji.getId();
		}
		return null;
	}

}
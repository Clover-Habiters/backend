package com.clover.habbittracker.domain.bookmark.api;

import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.domain.bookmark.dto.CreateBookmarkRequest;
import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.bookmark.repository.BookmarkRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

class BookmarkControllerTest extends RestDocsSupport {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	private Post savedPost;

	private Bookmark saveBookmark;

	@BeforeEach
	void setUp() {
		savedPost = postRepository.save(createTestPost(savedMember));

		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);
		saveBookmark = bookmarkRepository.save(bookmark);
	}

	@Test
	@DisplayName("[201 성공] 북마크 생성")
	void createBookmark() throws Exception {
		//given
		CreateBookmarkRequest request = new CreateBookmarkRequest("생성 테스트입니다.", "생성 테스트용입니다.");
		String bookmarkRequest = objectMapper.writeValueAsString(request);

		//when then
		mockMvc.perform(
				post("/bookmarks")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON)
					.content(bookmarkRequest))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/bookmarks/" + (saveBookmark.getId() + 1)))
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("title").type(STRING)
						.description("북마크 제목")
						.attributes(field("constraints", "30자 이내")),
					fieldWithPath("description").type(STRING)
						.description("북마크 설명")
						.attributes(field("constraints", "500자 이내"))
				)));
	}

	@Test
	@DisplayName("[204 성공] 북마크에 게시글 추가")
	void addPost() throws Exception {
		//when then
		mockMvc.perform(
				post("/bookmarks/{bookmarkId}/posts", saveBookmark.getId()) // 북마크 id는 옵셔넗 -> 디폴트 1(기본 북마크) 서비스 테스트에서 확인할 생각
					.header("Authorization", "Bearer " + accessToken)
					.queryParam("postId", String.valueOf(savedPost.getId()))
					.contentType(APPLICATION_JSON))
			.andExpect(status().isNoContent())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("bookmarkId").description("북마크 아이디").optional().description("기본 북마크는 1로 제공")
				),
				queryParameters(
					parameterWithName("postId").description("북마크에 추가할 게시글 아이디")
				)));
	}

	@Test
	@DisplayName("[200 성공] 북마크 전체 목록 조회")
	void getAllBookmarks() throws Exception {
		//given
		Bookmark bookmark = new Bookmark(savedMember, "조회 테스트입니다.", "조회 테스트용입니다.");
		bookmark.addPost(savedPost);
		bookmarkRepository.save(bookmark);

		//when then
		mockMvc.perform(
				get("/bookmarks")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("[].id").type(NUMBER).description("북마크 아이디"),
					fieldWithPath("[].title").type(STRING).description("북마크 제목"),
					fieldWithPath("[].description").type(STRING).description("북마크 설명"),
					fieldWithPath("[].posts").type(ARRAY).description("북마크 게시글 목록"),
					fieldWithPath("[].posts[].id").type(NUMBER).description("게시글 아이디"),
					fieldWithPath("[].posts[].title").type(STRING).description("게시글 제목"),
					fieldWithPath("[].posts[].category").type(STRING).description("게시글 카테고리")
				)));
	}

	@Test
	@DisplayName("[200 성공] 북마크 조회")
	void getBookmark() throws Exception {
		// given
		// when then
		mockMvc.perform(
				get("/bookmarks/{bookmarkId}", saveBookmark.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("bookmarkId").description("조회할 북마크 아이디")
				),
				responseFields(
					fieldWithPath("id").type(NUMBER).description("북마크 아이디"),
					fieldWithPath("title").type(STRING).description("북마크 제목"),
					fieldWithPath("description").type(STRING).description("북마크 설명"),
					fieldWithPath("posts").type(ARRAY).description("북마크 게시글 목록"),
					fieldWithPath("posts[].id").type(NUMBER).description("게시글 아이디"),
					fieldWithPath("posts[].title").type(STRING).description("게시글 제목"),
					fieldWithPath("posts[].category").type(STRING).description("게시글 카테고리")
				)));
	}

	@Test
	@DisplayName("[204 성공] 북마크 게시글 삭제")
	void deletePost() throws Exception {
		// given when then
		mockMvc.perform(
				delete("/bookmarks/{bookmarkId}/posts", saveBookmark.getId())
					.header("Authorization", "Bearer " + accessToken)
					.queryParam("postId", String.valueOf(savedPost.getId()))
					.contentType(APPLICATION_JSON))
			.andExpect(status().isNoContent())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("bookmarkId").description("북마크 아이디")
				),
				queryParameters(
					parameterWithName("postId").description("북마크에서 삭제할 게시글 아이디")
				)));
	}

	@Test
	@DisplayName("[204 성공] 북마크 삭제")
	void deleteBookmark() throws Exception {
		// given when then
		mockMvc.perform(
				delete("/bookmarks/{bookmarkId}", saveBookmark.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(APPLICATION_JSON))
			.andExpect(status().isNoContent())
			// restDocs 설정.
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				pathParameters(
					parameterWithName("bookmarkId").description("삭제할 북마크 아이디")
				)));
	}
}
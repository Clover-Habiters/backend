package com.clover.habbittracker.global.restdocs.common;

import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static com.clover.habbittracker.global.restdocs.util.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.global.restdocs.enums.EnumCode;
import com.clover.habbittracker.global.restdocs.enums.EnumResponse;
import com.fasterxml.jackson.core.type.TypeReference;

@DisplayName("RestDocs 공통 코드 문서화")
public class CommonDocumentation extends RestDocsSupport {

	@Test
	@DisplayName("공통 반환 필드")
	void commonResponse() throws Exception {
		mockMvc.perform(get("/test/docs/commons"))
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("code").description("결과 코드"),
					fieldWithPath("message").description("결과 메세지"),
					fieldWithPath("data").description("반환 데이터").attributes(field("type", "Object"))
				)
			));
	}

	@Test
	@DisplayName("enum 코드 문서화")
	void enums() throws Exception {
		ResultActions result = this.mockMvc.perform(
			get("/test/docs/enums")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// 결과값
		MvcResult mvcResult = result.andReturn();

		// 데이터 파싱
		EnumCode enumCode = getData(mvcResult);

		// 문서화 진행
		result.andExpect(status().isOk())
			.andDo(restDocs.document(
				customResponseFields("custom-response",
					beneathPath("data.emojiDomain").withSubsectionId("emojiDomain"),
					attributes(key("title").value("emojiDomain")),
					enumConvertFieldDescriptor((enumCode.getEmojiDomain()))
				),
				customResponseFields("custom-response", beneathPath("data.emojiType").withSubsectionId("emojiType"),
					attributes(key("title").value("emojiType")),
					enumConvertFieldDescriptor((enumCode.getEmojiType()))
				),
				customResponseFields("custom-response", beneathPath("data.category").withSubsectionId("category"),
					attributes(key("title").value("category")),
					enumConvertFieldDescriptor((enumCode.getCategory()))
				),
				customResponseFields("custom-response", beneathPath("data.searchType").withSubsectionId("searchType"),
					attributes(key("title").value("searchType")),
					enumConvertFieldDescriptor((enumCode.getSearchType()))
				)
			));
	}

	@Test
	@DisplayName("pageable 코드 문서화")
	void pageable() throws Exception {
		mockMvc.perform(
				get("/test/docs/pageable"))
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("content[]").type(ARRAY).description("page").ignored(),
					fieldWithPath("pageable.sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
					fieldWithPath("pageable.sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
					fieldWithPath("pageable.sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
					fieldWithPath("pageable.offset").type(NUMBER).description("페이지 오프셋"),
					fieldWithPath("pageable.pageNumber").type(NUMBER).description("페이지 번호"),
					fieldWithPath("pageable.pageSize").type(NUMBER).description("한 원소 수"),
					fieldWithPath("pageable.paged").type(BOOLEAN).description("페이지 정보 포함 여부"),
					fieldWithPath("pageable.unpaged").type(BOOLEAN).description("페이지 정보 비포함 여부\""),
					fieldWithPath("last").type(BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 개수"),
					fieldWithPath("totalElements").type(NUMBER).description("전체 데이터 개수"),
					fieldWithPath("number").type(NUMBER).description("페이지 번호"),
					fieldWithPath("sort.empty").type(BOOLEAN).description("빈 페이지 여부"),
					fieldWithPath("sort.sorted").type(BOOLEAN).description("페이지 정렬 여부"),
					fieldWithPath("sort.unsorted").type(BOOLEAN).description("페이지 비정렬 여부"),
					fieldWithPath("first").type(BOOLEAN).description("첫 번째 페이지 여부"),
					fieldWithPath("size").type(NUMBER).description("페이지 사이즈"),
					fieldWithPath("numberOfElements").type(NUMBER).description("페이지 원소 개수"),
					fieldWithPath("empty").type(BOOLEAN).description("빈 페이지 여부")
				)
			));
	}

	// mvc result 데이터 파싱
	private EnumCode getData(MvcResult result) throws IOException {
		EnumResponse<EnumCode> enumResponse = objectMapper
			.readValue(result.getResponse().getContentAsByteArray(),
				new TypeReference<EnumResponse<EnumCode>>() {
				}
			);
		return enumResponse.getData();
	}
}

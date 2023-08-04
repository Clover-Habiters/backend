package com.clover.habbittracker.global.restdocs.enums;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.global.restdocs.util.CustomResponseFieldsSnippet;
import com.fasterxml.jackson.core.type.TypeReference;

public class EnumDocumentation extends RestDocsSupport {

	@Test
	public void enums() throws Exception {
		ResultActions result = this.mockMvc.perform(
			get("/docs/enums")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// 결과값
		MvcResult mvcResult = result.andReturn();

		// 데이터 파싱
		EnumDocs enumDocs = getData(mvcResult);

		// 문서화 진행
		result.andExpect(status().isOk())
			.andDo(restDocs.document(
				customResponseFields("custom-response",
					beneathPath("data.emojiDomain").withSubsectionId("emojiDomain"),
					attributes(key("title").value("memberStatus")),
					enumConvertFieldDescriptor((enumDocs.getEmojiDomain()))
				),
				customResponseFields("custom-response", beneathPath("data.emojiType").withSubsectionId("emojiType"),
					attributes(key("title").value("emojiType")),
					enumConvertFieldDescriptor((enumDocs.getEmojiType()))
				),
				customResponseFields("custom-response", beneathPath("data.category").withSubsectionId("category"),
					attributes(key("title").value("category")),
					enumConvertFieldDescriptor((enumDocs.getCategory()))
				)
			));
	}

	// 커스텀 템플릿 사용을 위한 함수
	public static CustomResponseFieldsSnippet customResponseFields
	(String type,
		PayloadSubsectionExtractor<?> subsectionExtractor,
		Map<String, Object> attributes, FieldDescriptor... descriptors) {
		return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
			, true);
	}

	// Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
	private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
		return enumValues.entrySet().stream()
			.map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
			.toArray(FieldDescriptor[]::new);
	}

	// mvc result 데이터 파싱
	private EnumDocs getData(MvcResult result) throws IOException {
		EnumResponse<EnumDocs> enumResponse = objectMapper
			.readValue(result.getResponse().getContentAsByteArray(),
				new TypeReference<EnumResponse<EnumDocs>>() {
				}
			);
		return enumResponse.getData();
	}
}

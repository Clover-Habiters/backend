package com.clover.habbittracker.global.restdocs.util;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.util.Arrays;
import java.util.Map;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;

public class ApiDocumentUtils {

	private ApiDocumentUtils() {
		/* NO-OP */
	}

	public static OperationRequestPreprocessor getDocumentRequest() {
		return preprocessRequest(modifyUris().scheme("https").host("api.habiters.store").removePort(), prettyPrint());
	}

	public static OperationResponsePreprocessor getDocumentResponse() {
		return preprocessResponse(
			removeHeaders(
				"Vary",
				"X-Content-Type-Options",
				"X-XSS-Protection",
				"Cache-Control",
				"Pragma",
				"Expires",
				"Content-Length"
			),
			prettyPrint());
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
	public static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
		return enumValues.entrySet().stream()
			.map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
			.toArray(FieldDescriptor[]::new);
	}
}

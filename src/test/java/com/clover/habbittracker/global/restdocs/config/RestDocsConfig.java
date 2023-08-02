package com.clover.habbittracker.global.restdocs.config;

import static com.clover.habbittracker.global.restdocs.util.ApiDocumentUtils.*;
import static org.springframework.restdocs.snippet.Attributes.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

@TestConfiguration
public class RestDocsConfig {

	@Bean
	public RestDocumentationResultHandler write() {
		return MockMvcRestDocumentation.document(
			"{class-name}/{method-name}",
			getDocumentRequest(),
			getDocumentResponse()
		);
	}

	public static Attribute field(
		String key,
		String value) {
		return new Attribute(key, value);
	}
}

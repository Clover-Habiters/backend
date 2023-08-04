package com.clover.habbittracker.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.clover.habbittracker.global.restdocs.config.RestDocsConfig;

@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsSupport extends ControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@BeforeEach
	void setUp(WebApplicationContext context,
		RestDocumentationContextProvider provider) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(MockMvcRestDocumentation.documentationConfiguration(provider))  // rest docs 설정 주입
			.apply(SecurityMockMvcConfigurers.springSecurity())  // 스프링 시큐리티 적용
			.alwaysDo(MockMvcResultHandlers.print())
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
		logger.debug("Custom MockMvc Apply");
	}

}

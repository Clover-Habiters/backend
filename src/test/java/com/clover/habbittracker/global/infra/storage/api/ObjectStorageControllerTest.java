package com.clover.habbittracker.global.infra.storage.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.global.infra.storage.service.ObjectStorageService;

public class ObjectStorageControllerTest extends RestDocsSupport {

	@MockBean
	private ObjectStorageService objectStorageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(objectStorageService.imgSave(any(MultipartFile.class))).thenReturn("/success/url");
	}

	@Test
	@DisplayName("사진을 요청하여 저장할 수 있다.")
	void imageUploadTest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]);
		mockMvc.perform(multipart("/image/upload")
				.file(file))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestParts(
					partWithName("file").description("이미지 파일")
				),
				responseFields(
					fieldWithPath("code").description("결과 코드"),
					fieldWithPath("message").description("결과 메세지"),
					fieldWithPath("data").description("이미지 url")
				)
			));
	}
}

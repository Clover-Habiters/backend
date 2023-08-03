package com.clover.habbittracker.global.infra;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.clover.habbittracker.global.infra.storage.ObjectStorageService;

public class ObjectStorageServiceTest {
	private ObjectStorageService objectStorageService;

	@Mock
	private AmazonS3 mockStorage;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		objectStorageService = new ObjectStorageService(mockStorage);
	}

	@Test
	@DisplayName("multipartForm 데이터가 요청되었을때, 스토리지 서버에 저장한 후 파일이 저장된 url을 받는다.")
	public void naverCloudProfileImgSaveTest() throws IOException {
		//given
		MultipartFile mockFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
		String expectedBucketName = "user-profile-image";
		String mockUrl = "http://simple.url";

		//when
		when(mockStorage.putObject(any(PutObjectRequest.class))).thenReturn(null);
		when(mockStorage.getUrl(anyString(), anyString())).thenReturn(new URL(mockUrl));
		String result = objectStorageService.profileImgSave(mockFile);

		//then
		Assertions.assertThat(result).isEqualTo(mockUrl);
		verify(mockStorage).getUrl(eq(expectedBucketName), anyString());
	}

	@Test
	@DisplayName("multipartForm 데이터가 없다면 예외처리는 따로 하지않고, null을 반환한다.")
	public void failedProfileImgSaveTest() throws IOException {
		//given
		String mockUrl = "http://simple.url";

		//when
		when(mockStorage.putObject(any(PutObjectRequest.class))).thenReturn(null);
		when(mockStorage.getUrl(anyString(), anyString())).thenReturn(new URL(mockUrl));
		String result = objectStorageService.profileImgSave(null);

		//then
		Assertions.assertThat(result).isEqualTo(null);
	}
}

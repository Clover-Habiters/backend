package com.clover.habbittracker.global.infra.storage.api;

import static org.springframework.http.MediaType.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.habbittracker.global.base.dto.ApiResponse;
import com.clover.habbittracker.global.infra.storage.service.ObjectStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ObjectStorageController {
	private final ObjectStorageService objectStorageService;

	@PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
	ApiResponse<String> postImageUpload(
		@RequestPart("file") MultipartFile imageFile
	) {
		String imgUrl = objectStorageService.imgSave(imageFile);
		return ApiResponse.success(imgUrl);
	}
}

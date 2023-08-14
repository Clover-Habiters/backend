package com.clover.habbittracker.global.infra.storage.service;

import static com.clover.habbittracker.global.util.ImageUtil.*;

import java.io.File;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ObjectStorageService {

	private final AmazonS3 objectStorage;
	private final String BUCKET_NAME = "post-images";

	public String imgSave(MultipartFile file) {
		Optional.ofNullable(file).orElseThrow(IllegalArgumentException::new);
		File tempFile = tempFileSave(file);
		String imgUrl = objectStorageFileSave(tempFile);
		tempFileDelete(tempFile);
		return imgUrl;
	}

	private String objectStorageFileSave(File tempFile) {
		PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, tempFile.getName(), tempFile);
		request.withCannedAcl(CannedAccessControlList.PublicRead);
		objectStorage.putObject(request);
		return objectStorage.getUrl(BUCKET_NAME, tempFile.getName()).toString();
	}
}

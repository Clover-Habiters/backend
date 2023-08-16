package com.clover.habbittracker.global.infra.storage.service;

import static com.clover.habbittracker.global.util.ImageUtil.*;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ObjectStorageService {

	private final AmazonS3 objectStorage;
	private final String bucketName;

	public ObjectStorageService(AmazonS3 objectStorage,
		@Value("${NCP_BUCKET_NAME}") String bucketName) {
		this.objectStorage = objectStorage;
		this.bucketName = bucketName;
	}

	public String imgSave(MultipartFile file) {
		Optional.ofNullable(file).orElseThrow(IllegalArgumentException::new);
		File tempFile = tempFileSave(file);
		String imgUrl = objectStorageFileSave(tempFile);
		tempFileDelete(tempFile);
		return imgUrl;
	}

	private String objectStorageFileSave(File tempFile) {
		PutObjectRequest request = new PutObjectRequest(bucketName, tempFile.getName(), tempFile);
		request.withCannedAcl(CannedAccessControlList.PublicRead);
		objectStorage.putObject(request);
		return objectStorage.getUrl(bucketName, tempFile.getName()).toString();
	}
}

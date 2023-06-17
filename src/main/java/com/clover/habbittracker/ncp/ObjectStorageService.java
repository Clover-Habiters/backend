package com.clover.habbittracker.ncp;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

/* 2023/05/30 프로필 이미지를 사용하지 않기때문에 해당 서비스는 미사용
** 추후 다시 사용 할 수 있음 **
*/
@Service
@Slf4j
public class ObjectStorageService {

	private final AmazonS3 objectStorage;
	private final String imgPath;

	public ObjectStorageService(AmazonS3 objectStorage) {
		this.objectStorage = objectStorage;
		this.imgPath = System.getProperty("user.dir") + "/tmp";
	}

	public String profileImgSave(MultipartFile file) {
		if (file == null)
			return null;

		String buketName = "user-profile-image";
		String fileName = tempFileSave(file);
		PutObjectRequest request = new PutObjectRequest(buketName, fileName, new File(imgPath, fileName));
		request.withCannedAcl(CannedAccessControlList.PublicRead);
		objectStorage.putObject(request);

		if (!tempFileDelete(fileName)) {
			log.error("Failed to delete file. The administrator should check it out");
		}

		return objectStorage.getUrl(buketName, fileName).toString();
	}

	protected boolean tempFileDelete(String fileName) {
		File tempFile = new File(imgPath, fileName);
		if (tempFile.exists()) {
			return tempFile.delete();
		}
		return false;
	}

	protected String tempFileSave(MultipartFile file) {
		String uuid = UUID.randomUUID().toString().substring(0, 8);

		String imgName = uuid + "_" + file.getOriginalFilename();
		File imgFile = new File(imgPath, imgName);
		try {
			file.transferTo(imgFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return imgName;
	}
}

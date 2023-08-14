package com.clover.habbittracker.global.util;

import static lombok.AccessLevel.*;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ImageUtil {
	private static final String IMG_PATH = System.getProperty("user.dir") + "/tmp";

	public static void tempFileDelete(File tempFile) {
		if (tempFile.delete()) {
			log.error("임시 파일 삭제를 실패하였습니다. 해당 임시파일을 관리자가 직접 삭제해야 합니다. FileName = {}", tempFile.getName());
		}
	}

	public static File tempFileSave(MultipartFile file) {
		File imgFile = getImgFile(file);
		try {
			file.transferTo(imgFile);
		} catch (Exception e) {
			log.error("Failed to transfer file: {}", e.getMessage());
		}
		return imgFile;
	}

	private static File getImgFile(MultipartFile file) {
		String uuid = UUID.randomUUID().toString().substring(0, 8);

		String imgName = uuid + "_" + file.getOriginalFilename();
		return new File(IMG_PATH, imgName);
	}
}

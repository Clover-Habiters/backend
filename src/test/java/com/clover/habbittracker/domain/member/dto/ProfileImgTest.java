package com.clover.habbittracker.domain.member.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.domain.member.entity.ProfileImg;

public class ProfileImgTest {

	@Test
	@DisplayName("프로필 이미지를 랜덤으로 가져올 수 있다.")
	void randomProfileImgTest() {
		ProfileImg imgUrl = ProfileImg.getRandProfileImg();

		assertTrue(
			imgUrl == ProfileImg.HABITEE_DREAM ||
			imgUrl == ProfileImg.HABITEE_HOPE ||
			imgUrl == ProfileImg.HABITEE_LUCKY);
	}

}

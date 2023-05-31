package com.clover.habbittracker.domain.member.entity;

import java.util.Random;

import lombok.Getter;

@Getter
public enum ProfileImg {

	HABITEE_LUCKY("https://kr.object.ncloudstorage.com/user-profile-image/Habitee_Lucky.png"),
	HABITEE_HOPE("https://kr.object.ncloudstorage.com/user-profile-image/Habitee_Hope.png"),
	HABITEE_DREAM("https://kr.object.ncloudstorage.com/user-profile-image/Habitee_Dream.png");

	private final String imgUrl;

	ProfileImg(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public static ProfileImg getRandProfileImg() {
		return values()[new Random().nextInt(values().length)];
	}
}

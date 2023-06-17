package com.clover.habbittracker.domain.member.entity;

import java.util.Random;

import lombok.Getter;

@Getter
public enum ProfileImg {

	HABITEE_LUCKY("https://kr.object.ncloudstorage.com/habiters-bucket/Habitee_Lucky.svg"),
	HABITEE_HOPE("https://kr.object.ncloudstorage.com/habiters-bucket/Habitee_Hope.svg"),
	HABITEE_DREAM("https://kr.object.ncloudstorage.com/habiters-bucket/Habitee_Dream.svg");

	private final String imgUrl;

	ProfileImg(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public static ProfileImg getRandProfileImg() {
		return values()[new Random().nextInt(values().length)];
	}
}

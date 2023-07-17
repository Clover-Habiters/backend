package com.clover.habbittracker.global.util;

import com.clover.habbittracker.domain.member.entity.Member;

public class MemberProvider {
	private static final Long ID = 1L;
	private static final String EMAIL = "test@email.com";
	private static final String PROFILE_IMG_URL = "testImgUrl";
	private static final String NICK_NAME = "testNickName";
	private static final String OAUTH_ID = "testOauthId";
	private static final String PROVIDER = "testProvider";

	public static Member createTestMember() {
		return Member.builder()
			.email(EMAIL)
			.oauthId(OAUTH_ID)
			.profileImgUrl(PROFILE_IMG_URL)
			.provider(PROVIDER)
			.nickName(NICK_NAME)
			.build();
	}

	public static Member createTestMember(Long memberNum) {
		return Member.builder()
			.id(memberNum)
			.email(EMAIL + memberNum)
			.oauthId(OAUTH_ID + memberNum)
			.profileImgUrl(PROFILE_IMG_URL + memberNum)
			.provider(PROVIDER + memberNum)
			.nickName(NICK_NAME + memberNum)
			.build();
	}

	public static Long getId() {
		return ID;
	}

	public static Long getId(int memberNum) {
		return ID + memberNum;
	}

	public static String getNickName() {
		return NICK_NAME;
	}

	public static String getNickName(int memberNum) {
		return NICK_NAME + memberNum;
	}
}

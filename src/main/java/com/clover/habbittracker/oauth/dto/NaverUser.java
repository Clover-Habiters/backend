package com.clover.habbittracker.oauth.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;

@Builder
public class NaverUser implements SocialUser {
	String email;
	String nickName;
	String provider;
	String oauthId;
	String profileImgUrl;

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getNickName() {
		return this.nickName;
	}

	@Override
	public String getProvider() {
		return this.provider;
	}

	@Override
	public String getOauthId() {
		return this.oauthId;
	}

	@Override
	public String getProfileImgUrl() {
		return this.profileImgUrl;
	}

	public static SocialUser info(OAuth2User oAuth2User) {
		Map<String, Object> response = oAuth2User.getAttribute("response");

		return NaverUser.builder()
			.email(String.valueOf(response.get("email")))
			.nickName(String.valueOf(response.get("name")))
			.oauthId(String.valueOf(response.get("id")))
			.provider("naver")
			.profileImgUrl(String.valueOf(response.get("profile_image")))
			.build();

	}
}

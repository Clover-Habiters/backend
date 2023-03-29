package com.clover.habbittracker.global.oauth.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;

@Builder
public class KakaoUser implements SocialUser{
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
		Map<String, Object> response = oAuth2User.getAttributes();
		Map<String, Object> properties = oAuth2User.getAttribute("properties");
		Map<String, Object> account = oAuth2User.getAttribute("kakao_account");

		return KakaoUser.builder()
			.email(String.valueOf(account.get("email")))
			.nickName(String.valueOf(properties.get("nickname")))
			.oauthId(String.valueOf(response.get("id")))
			.provider("kakao")
			.profileImgUrl(String.valueOf(properties.get("profile_image")))
			.build();
	}
}

package com.clover.habbittracker.global.security.oauth.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;

@Builder
public class GoogleUser implements SocialUser{
	String email;
	String nickName;
	String provider;
	String oauthId;

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

	public static SocialUser info(OAuth2User oAuth2User) {
		Map<String, Object> response = oAuth2User.getAttributes();

		return GoogleUser.builder()
			.email(String.valueOf(response.get("email")))
			.nickName(String.valueOf(response.get("name")))
			.oauthId(String.valueOf(response.get("sub")))
			.provider("google")
			.build();
	}
}

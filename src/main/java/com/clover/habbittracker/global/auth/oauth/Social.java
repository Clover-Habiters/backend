package com.clover.habbittracker.global.auth.oauth;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.clover.habbittracker.global.auth.oauth.dto.SocialUser;

import lombok.Getter;

@Getter
public enum Social {
	KAKAO("kakao") {
		@Override
		public SocialUser toUserInfo(OAuth2User oAuth2User) {
			Map<String, Object> response = oAuth2User.getAttributes();
			Map<String, Object> properties = oAuth2User.getAttribute("properties");
			Map<String, Object> account = oAuth2User.getAttribute("kakao_account");

			return SocialUser.builder()
				.email(String.valueOf(account.get("email")))
				.nickName(String.valueOf(properties.get("nickname")))
				.oauthId(String.valueOf(response.get("id")))
				.provider(getProvider())
				.build();
		}
	},

	NAVER("naver") {
		@Override
		public SocialUser toUserInfo(OAuth2User oAuth2User) {
			Map<String, Object> response = oAuth2User.getAttribute("response");

			return SocialUser.builder()
				.email(String.valueOf(response.get("email")))
				.nickName(String.valueOf(response.get("name")))
				.oauthId(String.valueOf(response.get("id")))
				.provider(getProvider())
				.build();
		}
	},

	GOOGLE("google") {
		@Override
		public SocialUser toUserInfo(OAuth2User oAuth2User) {
			Map<String, Object> response = oAuth2User.getAttributes();

			return SocialUser.builder()
				.email(String.valueOf(response.get("email")))
				.nickName(String.valueOf(response.get("name")))
				.oauthId(String.valueOf(response.get("sub")))
				.provider(getProvider())
				.build();
		}
	};

	private final String provider;

	Social(String provider) {
		this.provider = provider;
	}

	public abstract SocialUser toUserInfo(OAuth2User oAuth2User);
}
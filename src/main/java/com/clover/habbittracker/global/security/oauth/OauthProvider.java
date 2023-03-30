package com.clover.habbittracker.global.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.clover.habbittracker.global.security.oauth.dto.GoogleUser;
import com.clover.habbittracker.global.security.oauth.dto.KakaoUser;
import com.clover.habbittracker.global.security.oauth.dto.NaverUser;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

public class OauthProvider {

	public static SocialUser getProfile(OAuth2User oAuth2User, String provider) {
		switch (provider) {
			case "kakao" -> {
				return KakaoUser.info(oAuth2User);
			}
			case "naver" -> {
				return NaverUser.info(oAuth2User);
			}
			case "google" -> {
				return GoogleUser.info(oAuth2User);
			}
			default -> throw new IllegalArgumentException();
		}
	}
}

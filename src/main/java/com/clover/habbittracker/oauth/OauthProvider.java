package com.clover.habbittracker.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.clover.habbittracker.oauth.dto.GoogleUser;
import com.clover.habbittracker.oauth.dto.KakaoUser;
import com.clover.habbittracker.oauth.dto.NaverUser;
import com.clover.habbittracker.oauth.dto.SocialUser;

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

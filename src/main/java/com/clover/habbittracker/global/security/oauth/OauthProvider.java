package com.clover.habbittracker.global.security.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

public class OauthProvider {
	public static final Map<String, Social> socialMap = new HashMap<>();

	static {
		for(Social social : Social.values()) {
			socialMap.put(social.getProvider(), social);
		}
	}

	public static SocialUser getSocialUser(OAuth2User oAuth2User, String provider) {
		Social social = socialMap.get(provider);
		return social.toSocialUser(oAuth2User);
	}
}

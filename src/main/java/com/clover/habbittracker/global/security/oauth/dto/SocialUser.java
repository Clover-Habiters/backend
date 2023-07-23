package com.clover.habbittracker.global.security.oauth.dto;

import lombok.Builder;

@Builder
public record SocialUser(String email, String nickName, String provider, String oauthId) {
	@Override
	public String nickName() {

		if (nickName == null) {
			return null;
		}

		if (nickName.length() > 8) {
			nickName.replace(" ", "").substring(0, 8);
		}
		return nickName;
	}
}

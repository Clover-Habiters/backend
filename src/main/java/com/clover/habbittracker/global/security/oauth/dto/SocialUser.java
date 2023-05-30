package com.clover.habbittracker.global.security.oauth.dto;



public interface SocialUser {
	String getEmail();

	String getNickName();

	String getProvider();

	String getOauthId();
	//
	// String getProfileImgUrl();
}

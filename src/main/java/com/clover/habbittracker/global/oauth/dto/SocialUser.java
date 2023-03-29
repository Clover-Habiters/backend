package com.clover.habbittracker.global.oauth.dto;



public interface SocialUser {
	String getEmail();

	String getNickName();

	String getProvider();

	String getOauthId();

	String getProfileImgUrl();
}

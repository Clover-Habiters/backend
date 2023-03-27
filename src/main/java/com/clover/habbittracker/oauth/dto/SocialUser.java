package com.clover.habbittracker.oauth.dto;



public interface SocialUser {
	String getEmail();

	String getNickName();

	String getProvider();

	String getOauthId();

	String getProfileImgUrl();
}

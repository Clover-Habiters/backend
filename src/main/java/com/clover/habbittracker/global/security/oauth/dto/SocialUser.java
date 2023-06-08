package com.clover.habbittracker.global.security.oauth.dto;



public interface SocialUser {
	String getEmail();

	String getNickName();

	String getProvider();

	String getOauthId();

	default String trimNickName(String nickName) {

		if(nickName == null) {
			return null;
		}

		if(nickName.length() > 8){
			nickName = nickName.replace(" ","").substring(0, 8);
		}
		return nickName;
	}
}

package com.clover.habbittracker.global.auth.oauth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.clover.habbittracker.global.auth.oauth.dto.SocialUser;
import com.clover.habbittracker.global.auth.oauth.util.OauthUserProvider;

class SocialTest {

	@ParameterizedTest
	@DisplayName("제공자 아이디에 맞는 socialUser 를 생성한다.")
	@ValueSource(strings = {"kakao", "naver", "google"})
	void toUserInfoTest(String provider) {
		//given
		OAuth2User oauthUser = OauthUserProvider.getOauthUser(provider);

		//when
		SocialUser socialUser = Social.valueOf(provider.toUpperCase()).toUserInfo(oauthUser);

		//then
		assertAll(() -> {
				assertThat(socialUser.provider()).isEqualTo(provider);
				assertThat(socialUser.oauthId()).isEqualTo(provider + "_oauthId");
				assertThat(socialUser.email()).isEqualTo(provider + "_email");
				assertThat(socialUser.nickName()).isEqualTo(provider + "_nickname");
			}
		);
	}

}

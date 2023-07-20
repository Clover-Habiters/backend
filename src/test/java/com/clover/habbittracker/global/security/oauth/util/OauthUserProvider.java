package com.clover.habbittracker.global.security.oauth.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OauthUserProvider {

	private static final Map<String, OAuth2User> oauthUserMap = new HashMap<>();

	static {
		oauthUserMap.put("kakao", new OauthKakao());
		oauthUserMap.put("naver", new OauthNaver());
		oauthUserMap.put("google", new OauthGoogle());
	}

	public static OAuth2User getOauthUser(String provider) {
		return oauthUserMap.get(provider);
	}

	static class OauthKakao implements OAuth2User {

		@Override
		public <A> A getAttribute(String name) {
			if (name.equals("properties")) {
				Map<String, Object> properties = new HashMap<>();
				properties.put("nickname", "kakao_nickname");
				return (A) properties;
			} else if (name.equals("kakao_account")) {
				Map<String, Object> account = new HashMap<>();
				account.put("email", "kakao_email");
				return (A) account;
			}
			return OAuth2User.super.getAttribute(name);
		}

		@Override
		public Map<String, Object> getAttributes() {
			Map<String, Object> response = new HashMap<>();
			response.put("id", "kakao_oauthId");
			return response;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}
	}

	static class OauthNaver implements OAuth2User {

		@Override
		public <A> A getAttribute(String name) {
			Map<String, Object> response = new HashMap<>();
			response.put("id","naver_oauthId");
			response.put("email","naver_email");
			response.put("name", "naver_nickname");
			return (A) response;
		}

		@Override
		public Map<String, Object> getAttributes() {
			return null;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}
	}

	static class OauthGoogle implements OAuth2User {

		@Override
		public <A> A getAttribute(String name) {
			return OAuth2User.super.getAttribute(name);
		}

		@Override
		public Map<String, Object> getAttributes() {
			Map<String, Object> response = new HashMap<>();
			response.put("sub", "google_oauthId");
			response.put("email","google_email");
			response.put("name", "google_nickname");
			return response;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}
	}
}

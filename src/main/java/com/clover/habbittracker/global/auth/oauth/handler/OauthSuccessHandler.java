package com.clover.habbittracker.global.auth.oauth.handler;

import static com.clover.habbittracker.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.clover.habbittracker.global.auth.oauth.Social;
import com.clover.habbittracker.global.auth.oauth.dto.SocialUser;
import com.clover.habbittracker.global.auth.oauth.service.OauthService;
import com.clover.habbittracker.global.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OauthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private final OauthService oauthService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		String[] splitURI = request.getRequestURI().split("/");
		String provider = splitURI[splitURI.length - 1].toUpperCase(); // URI 맨 뒤는 제공자
		Social social = Social.valueOf(provider);
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User user) {
			SocialUser userInfo = social.toUserInfo(user);
			String accessToken = oauthService.login(userInfo);
			String targetUrl = determineTargetUrl(request, accessToken);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}

	}

	private String determineTargetUrl(HttpServletRequest request, String accessToken) {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken) // url에도 실어 보내기
			.build().toUriString();
	}
}

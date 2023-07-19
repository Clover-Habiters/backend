package com.clover.habbittracker.global.security.oauth;

import static com.clover.habbittracker.global.security.oauth.HttpCookieOAuthAuthorizationRequestRepository.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.clover.habbittracker.global.security.oauth.dto.SocialUser;
import com.clover.habbittracker.global.util.CookieUtil;

import jakarta.servlet.ServletException;
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
		Authentication authentication) throws IOException, ServletException {
		String[] splitURI = request.getRequestURI().split("/");
		String provider = splitURI[splitURI.length - 1]; // URI 맨 뒤는 제공자
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User user) {
			SocialUser userInfo = OauthProvider.getSocialUser(user, provider);
			String accessToken = oauthService.login(userInfo);
			String targetUrl = determineTargetUrl(request,accessToken);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}

	}

	private String determineTargetUrl(HttpServletRequest request,String accessToken) {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken) // url에도 실어 보내기
			.build().toUriString();
	}
}

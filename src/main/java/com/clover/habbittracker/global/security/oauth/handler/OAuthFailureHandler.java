package com.clover.habbittracker.global.security.oauth.handler;


import static com.clover.habbittracker.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.clover.habbittracker.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.clover.habbittracker.global.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthFailureHandler
	extends SimpleUrlAuthenticationFailureHandler {

	private static final String DEFAULT_TARGET_URL = "/";
	private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		String redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(DEFAULT_TARGET_URL);

		String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
			.queryParam("error", exception.getMessage())
			.build().toUriString();

		httpCookieOAuthAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}



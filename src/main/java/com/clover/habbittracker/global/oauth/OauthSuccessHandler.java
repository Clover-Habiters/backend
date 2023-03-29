package com.clover.habbittracker.global.oauth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.clover.habbittracker.global.oauth.dto.SocialUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {
	private final OauthService oauthService;

	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String[] splitURI = request.getRequestURI().split("/");
		String provider = splitURI[splitURI.length - 1]; // URI 맨 뒤는 제공자
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User user) {
			SocialUser userInfo = OauthProvider.getProfile(user, provider);
			String accessToken = oauthService.login(userInfo);
			response.setStatus(HttpStatus.OK.value());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(accessToken));

		}

	}
}

package com.clover.habbittracker.global.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.ServletException;

public class JwtFilterTest {
	private final String SECRET_KEY = "jwt.secret.test.createToken";
	private final Long EXPIRED_MS = 10000L;
	private final Long REFRESH_EXPIRED_MS = 20000L;
	private JwtProvider jwtProvider;
	private JwtFilter jwtFilter;
	private MockHttpServletResponse httpServletResponse;
	private MockHttpServletRequest httpServletRequest;
	private MockFilterChain filterChain;

	@BeforeEach
	void setUp() {
		httpServletRequest = new MockHttpServletRequest();
		httpServletResponse = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
		jwtProvider = new JwtProvider(SECRET_KEY, EXPIRED_MS, REFRESH_EXPIRED_MS);
		jwtFilter = new JwtFilter(jwtProvider);
	}

	@Test
	@DisplayName("Request헤더에 Authorization타입과 Bearer accessToken이 요청온다면 정상적으로 인증을 한 후 SecurityContextHolder에 저장된다.")
	void successAccessTokenFilter() throws ServletException, IOException {
		//given
		Long memberId = 1L;
		String accessJwt = jwtProvider.createAccessJwt(memberId);
		httpServletRequest.addHeader("Authorization", "Bearer " + accessJwt);
		//when
		assertDoesNotThrow(() -> jwtFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//then
		assertThat(authentication.getPrincipal()).isEqualTo(memberId);
	}

	@Test
	@DisplayName("Bearer 또는 accessToken이 없다면 예외로 던진다.")
	void failedAccessTokenFilter() {
		//given
		Long memberId = 1L;
		String accessJwt = jwtProvider.createAccessJwt(memberId);
		//Bearer가 없다면 예외 처리.
		httpServletRequest.addHeader("Authorization", accessJwt);
		assertThrows(JwtException.class,
			() -> jwtFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain));

		//AccessToken이 없다면 예외 처리.
		httpServletRequest.addHeader("Authorization", "Bearer ");
		assertThrows(JwtException.class,
			() -> jwtFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain));
	}

	@Test
	@DisplayName("인증이 필요하지 않다면, 필터에 걸리지않게 한다.")
	void noAuthorizationFiler() {
		//when
		boolean shouldNotFilter = jwtFilter.shouldNotFilter(httpServletRequest);
		//then
		assertThat(shouldNotFilter).isTrue();

	}
}

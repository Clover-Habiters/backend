package com.clover.habbittracker.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;


	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().endsWith("token") && request.getMethod().equalsIgnoreCase("POST");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// request Header에서 토큰 값 가져오기.
		String token = getAccessToken(request);

		if(token != null) {
			// 권한 부여
			UsernamePasswordAuthenticationToken authenticationToken = createAuthentication(token);
			// userDetail 작성
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		filterChain.doFilter(request, response);
	}

	public String getAccessToken(HttpServletRequest request) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 토큰 구조를 확인 후 잘못된 구조라면 null 반환.
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return null;
		}
		String token = authorization.split(" ")[1];

		// 가져온 토큰값이 널이 아니라면 유효성 검증. 유효성 문제가 있을 경우 예외처리.
		if (token != null)
			jwtProvider.validOf(token);

		return token;
	}

	public UsernamePasswordAuthenticationToken createAuthentication(String token) {
		Claims payload = jwtProvider.getClaims(token);
		Long userId = payload.get("userId", Long.class);

		return new UsernamePasswordAuthenticationToken(userId,
			null, List.of(new SimpleGrantedAuthority("ROLE_MEMBER")));
	}
}

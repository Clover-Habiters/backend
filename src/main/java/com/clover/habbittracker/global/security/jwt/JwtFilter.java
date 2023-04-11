package com.clover.habbittracker.global.security.jwt;

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
		return request.getRequestURI().endsWith("token") || request.getHeader(HttpHeaders.AUTHORIZATION) == null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// request Header에서 토큰 값 가져오기.
		String token = getAccessToken(request);

		// 권한 부여
		UsernamePasswordAuthenticationToken authenticationToken = createAuthentication(token);
		// userDetail 작성
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}

	public String getAccessToken(HttpServletRequest request) {
		String[] authorization = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
		String tokenType = authorization[0];

		if(!tokenType.startsWith("Bearer") || authorization.length < 2){
			throw new JwtException("잘못된 토큰 구조입니다.");
		}

		String token = authorization[1];
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

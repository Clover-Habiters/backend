package com.clover.habbittracker.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;


import com.clover.habbittracker.global.security.jwt.JwtAuthenticationEntryPoint;
import com.clover.habbittracker.global.security.jwt.JwtFilter;
import com.clover.habbittracker.global.security.oauth.HttpCookieOAuthAuthorizationRequestRepository;
import com.clover.habbittracker.global.security.oauth.OAuthFailureHandler;
import com.clover.habbittracker.global.security.oauth.OauthSuccessHandler;


import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtFilter jwtFilter;

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	private final OauthSuccessHandler oauthSuccessHandler;

	private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;

	private final OAuthFailureHandler oAuthFailureHandler;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
			.csrf().disable()
			.headers().frameOptions().disable().and() //h2-console 접속을 위해 임시 설정. 추후에 데이터베이스 변경시 삭제 예정
			.formLogin().disable()  //formLogin 페이지를 사용하지 않음.
			.httpBasic().disable()  //http 기본 검증을 사용하지 않음. Bearer 사용
			.cors().and()  // cors 설정
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests()
			.requestMatchers("/users/**").authenticated()
			.requestMatchers("/diaries/**").authenticated()
			.requestMatchers("/diaries").authenticated()
			.requestMatchers("/habits").authenticated()
			.requestMatchers("/habits/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.oauth2Login()
			.authorizationEndpoint()
			.baseUri("/oauth2/authorization")
			.authorizationRequestRepository(httpCookieOAuthAuthorizationRequestRepository)
			.and()
			.successHandler(oauthSuccessHandler)
			.failureHandler(oAuthFailureHandler)
			.and()
			.addFilterBefore(jwtFilter, OAuth2AuthorizationRequestRedirectFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

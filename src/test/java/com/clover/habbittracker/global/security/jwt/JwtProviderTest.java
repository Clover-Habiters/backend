package com.clover.habbittracker.global.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtProviderTest {
	JwtProvider jwtProvider = new JwtProvider("jwt.secret.test.createToken", 1000L, 60000L);

	@Test
	@DisplayName("UserID를 이용하여 JWT 토큰을 만든다.")
	void createToken() {
		assertDoesNotThrow(() -> jwtProvider.createAccessJwt(1L));
	}

	@Test
	@DisplayName("UserId로 토큰을 만들어도 토큰값은 항상 다르다")
	void createTokenSameIdNotEqual() {
		assertThat(jwtProvider.createAccessJwt(1L)).isNotSameAs(jwtProvider.createAccessJwt(1L));
	}

	@Test
	@DisplayName("토큰을 이용하여 UserId를 찾을 수 있다.")
	void tokenFindByUserId() {
		Long memberId = 1L;
		String accessJwt = jwtProvider.createAccessJwt(memberId);
		Claims claims = jwtProvider.getClaims(accessJwt);
		Long userId = claims.get("userId", Long.class);
		assertThat(memberId).isEqualTo(userId);
	}
	@Test
	@DisplayName("토큰의 유효시간을 검증 할 수 있다.")
	void tokenValidOfExpired() {
		Long memberId = 1L;
		String accessJwt = jwtProvider.createAccessJwt(memberId);

		assertDoesNotThrow(() -> jwtProvider.validOf(accessJwt));
	}
	@Test
	@DisplayName("토큰의 유효시간이 지나가면 예외가 터진다.")
	void tokenExpiredException() throws InterruptedException {
		String accessJwt = jwtProvider.createAccessJwt(1L);

		Thread.sleep(1100L);

		assertThrows(JwtException.class , () -> jwtProvider.validOf(accessJwt));

	}
}

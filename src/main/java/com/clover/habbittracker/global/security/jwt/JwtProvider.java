package com.clover.habbittracker.global.security.jwt;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.clover.habbittracker.global.security.exception.JwtExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	private final String SECRET_KEY;
	private final Long ACCESS_EXPIRED_MS;

	private final Long REFRESH_EXPIRED_MS;

	public JwtProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiredMs}") Long expiredMs,
		@Value("${jwt.refreshExpiredMs}") Long refreshExpiredMs) {
		this.SECRET_KEY = secretKey;
		this.ACCESS_EXPIRED_MS = expiredMs;
		this.REFRESH_EXPIRED_MS = refreshExpiredMs;
	}

	public String createAccessJwt(Long userId) {
		Claims claims = Jwts.claims();
		claims.put("userId", userId);

		return Jwts.builder().setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRED_MS))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
			.compact();
	}

	public Claims getClaims(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new JwtExpiredException(token);
		}

	}

	// public void validOf(String token) {
	// 	try {
	// 		Jwts.parser()
	// 			.setSigningKey(SECRET_KEY)
	// 			.parseClaimsJws(token);
	// 	} catch (ExpiredJwtException e) {
	// 		throw new JwtStructureException();
	// 	}
	// }

	public String createRefreshJwt() {
		Claims claims = Jwts.claims();
		String payload = UUID.randomUUID().toString();
		claims.put("payload", payload);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRED_MS))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
			.compact();
	}

}

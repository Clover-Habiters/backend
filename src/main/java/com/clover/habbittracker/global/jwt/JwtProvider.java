package com.clover.habbittracker.global.jwt;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	private final String SECRET_KEY;
	private final Long ACCESS_EXPIRED_MS;

	private final Long REFRESH_EXPIRED_MS;

	public JwtProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiredMs}") Long expiredMs,@Value("${jwt.refreshExpiredMs}") Long refreshExpiredMs) {
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
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public void validOf(String token) {
		try{
			Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token);
  		}catch (ExpiredJwtException e){
			throw new JwtException("유효시간이 만료된 토큰입니다.");
  		}
	}

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

package com.clover.habbittracker.global.security.oauth;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.clover.habbittracker.domain.member.service.MemberService;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {

	private final JwtProvider jwtProvider;

	private final MemberService memberService;

	public String login(SocialUser socialUser) {
		Long userId = memberService.join(socialUser);
		System.out.println(userId);
		return jwtProvider.createAccessJwt(userId);
	}
}

package com.clover.habbittracker.oauth;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.jwt.JwtProvider;
import com.clover.habbittracker.member.service.MemberService;
import com.clover.habbittracker.oauth.dto.SocialUser;

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

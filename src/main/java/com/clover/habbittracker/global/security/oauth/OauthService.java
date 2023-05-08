package com.clover.habbittracker.global.security.oauth;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.clover.habbittracker.domain.member.service.MemberService;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

	private final JwtProvider jwtProvider;

	private final MemberService memberService;

	private final String LOG_FORM = "Success Login Id : {}, Name : {}, Provider : {}";

	public String login(SocialUser socialUser) {
		Long userId = memberService.join(socialUser);
		log.info(LOG_FORM,userId,socialUser.getNickName(),socialUser.getProvider());
		return jwtProvider.createAccessJwt(userId);
	}
}

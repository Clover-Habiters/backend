package com.clover.habbittracker.global.security.oauth;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.entity.ProfileImg;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

	private final JwtProvider jwtProvider;

	private final MemberRepository memberRepository;

	private final String LOGIN_FORM = "Success Login Id : {}, Name : {}, Provider : {}";

	private final String LOGOUT_FORM = "Success Logout Id : {}, Name : {}, Provider : {}";

	public String login(SocialUser socialUser) {
		Long userId = memberRepository.findByProviderAndOauthId(socialUser.provider(), socialUser.oauthId())
			.map(Member::getId)
			.orElseGet(() -> register(socialUser).getId());
		log.info(LOGIN_FORM,userId,socialUser.nickName(),socialUser.provider());
		return jwtProvider.createAccessJwt(userId);
	}

	public void logout(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));


	}

	private Member register(SocialUser socialUser) {
		return memberRepository.save(
			Member.builder()
				.email(socialUser.email())
				.oauthId(socialUser.oauthId())
				.nickName("해비터_" + socialUser.nickName())
				.provider(socialUser.provider())
				.profileImgUrl(ProfileImg.getRandProfileImg().getImgUrl())
				.build());
	}
}

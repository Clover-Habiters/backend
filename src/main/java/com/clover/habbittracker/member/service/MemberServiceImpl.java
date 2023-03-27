package com.clover.habbittracker.member.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.member.dto.MemberResponse;
import com.clover.habbittracker.member.entity.Member;
import com.clover.habbittracker.oauth.dto.SocialUser;
import com.clover.habbittracker.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;

	@Override
	public Long join(SocialUser socialUser) {
		return memberRepository.findByProviderAndOauthId(socialUser.getProvider(), socialUser.getOauthId())
			.map(Member::getId)
			.orElseGet(() -> register(socialUser).getId());
	}

	@Override
	public MemberResponse getProfile(Long memberId) {
		return memberRepository.findById(memberId)
			.map(MemberResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지않습니다."));
	}

	private Member register(SocialUser socialUser) {
		return memberRepository.save(
			Member.builder()
				.email(socialUser.getEmail())
				.oauthId(socialUser.getOauthId())
				.nickName("해빗터_"+socialUser.getNickName())
				.provider(socialUser.getProvider())
				.profileImgUrl(socialUser.getProfileImgUrl())
				.build());
	}
}

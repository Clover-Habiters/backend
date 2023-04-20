package com.clover.habbittracker.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

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
			.orElseThrow(MemberNotFoundException::new);
	}

	@Override
	@Transactional
	public MemberResponse updateProfile(Long memberId, MemberRequest request) {
		return memberRepository.findById(memberId)
			.map(member -> update(member, request))
			.map(MemberResponse::from)
			.orElseThrow();

	}

	@Override
	public void deleteProfile(Long memberId) {
		memberRepository.deleteById(memberId);
	}

	private Member register(SocialUser socialUser) {
		return memberRepository.save(
			Member.builder()
				.email(socialUser.getEmail())
				.oauthId(socialUser.getOauthId())
				.nickName("해빗터_" + socialUser.getNickName())
				.provider(socialUser.getProvider())
				.profileImgUrl(socialUser.getProfileImgUrl())
				.build());
	}

	private Member update(Member member, MemberRequest request) {
		Optional<Member> byNickName = memberRepository.findByNickName(request.getNickName());
		if (byNickName.isEmpty()) {
			Optional.ofNullable(request.getProfileImgUrl()).ifPresent(member::setProfileImgUrl);
			Optional.ofNullable(request.getNickName()).ifPresent(member::setNickName);
			return member;
		} else {
			throw new IllegalArgumentException();
		}
	}
}

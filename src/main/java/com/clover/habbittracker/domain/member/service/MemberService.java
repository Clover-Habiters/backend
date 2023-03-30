package com.clover.habbittracker.domain.member.service;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

public interface MemberService {
	Long join(SocialUser socialUser);
	MemberResponse getProfile(Long memberId);

	MemberResponse updateProfile(Long memberId, MemberRequest request);

	void deleteProfile(Long memberId);
}

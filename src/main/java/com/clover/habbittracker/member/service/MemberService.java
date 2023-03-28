package com.clover.habbittracker.member.service;

import com.clover.habbittracker.member.dto.MemberRequest;
import com.clover.habbittracker.member.dto.MemberResponse;
import com.clover.habbittracker.oauth.dto.SocialUser;

public interface MemberService {
	Long join(SocialUser socialUser);
	MemberResponse getProfile(Long memberId);

	MemberResponse updateProfile(Long memberId, MemberRequest request);

	void deleteProfile(Long memberId);
}

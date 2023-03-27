package com.clover.habbittracker.member.service;

import com.clover.habbittracker.member.dto.MemberResponse;
import com.clover.habbittracker.oauth.dto.SocialUser;

public interface MemberService {
	Long join(SocialUser socialUser);
	MemberResponse getProfile(Long memberId);
}

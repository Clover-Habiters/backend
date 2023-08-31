package com.clover.habbittracker.domain.member.service;

import com.clover.habbittracker.domain.member.dto.MemberReportResponse;
import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;

public interface MemberService {
	MemberResponse getProfile(Long memberId);

	MemberResponse updateProfile(Long memberId, MemberRequest request);

	void deleteProfile(Long memberId);

	MemberReportResponse getMyReport(Long memberId);
}

package com.clover.habbittracker.domain.member.repository;

import java.util.Optional;

import com.clover.habbittracker.domain.member.dto.MemberReportResponse;
import com.clover.habbittracker.domain.member.entity.Member;

public interface MemberCustomRepository {

	Optional<Member> findByProviderAndOauthId(String provider, String oauthId);

	Optional<Member> findByNickName(String nickName);

	MemberReportResponse findReportById(Long memberId);
}

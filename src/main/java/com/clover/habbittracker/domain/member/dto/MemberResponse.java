package com.clover.habbittracker.domain.member.dto;

import com.clover.habbittracker.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponse {
	Long id;
	String email;
	String nickName;
	String profileImgUrl;

	public static MemberResponse from(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickName(member.getNickName())
			.profileImgUrl(member.getProfileImgUrl())
			.build();
	}
}

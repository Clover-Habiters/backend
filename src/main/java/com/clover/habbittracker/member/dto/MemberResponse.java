package com.clover.habbittracker.member.dto;

import com.clover.habbittracker.member.entity.Member;

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

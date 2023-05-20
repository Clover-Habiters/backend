package com.clover.habbittracker.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MemberRequest {
	String nickName;
	String profileImgUrl;
}

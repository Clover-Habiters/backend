package com.clover.habbittracker.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberRequest {
	String nickName;
	String profileImgUrl;
}

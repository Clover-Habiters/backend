package com.clover.habbittracker.domain.member.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
	@Size(max = 12,message = "닉네임은 12자 이내로 입력해주세요.")
	String nickName;
}

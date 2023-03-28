package com.clover.habbittracker.habbit.dto;

import java.time.LocalDateTime;

import com.clover.habbittracker.habbit.entity.Habbit;
import com.clover.habbittracker.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HabbitRespose {
	private Long id;
	private String content;
	private Member member;
	private LocalDateTime createDate;

	public static HabbitRespose from(Habbit habbit) {
		return HabbitRespose.builder()
			.id(habbit.getId())
			.content(habbit.getContent())
			.createDate(habbit.getCreatedAt())
			.member(habbit.getMember())
			.build();
	}
}

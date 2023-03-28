package com.clover.habbittracker.habbit.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.habbit.dto.HabbitRequest;
import com.clover.habbittracker.habbit.dto.HabbitRespose;
import com.clover.habbittracker.habbit.entity.Habbit;
import com.clover.habbittracker.habbit.repository.HabbitRepository;
import com.clover.habbittracker.member.entity.Member;
import com.clover.habbittracker.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabbitServiceImpl implements HabbitService {

	private final HabbitRepository habbitRepository;

	private final MemberRepository memberRepository;

	@Override
	public HabbitRespose register(Long memberId, HabbitRequest request) {
		Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
		Habbit habbit = Habbit.builder()
			.content(request.getContent())
			.member(member).build();
		return HabbitRespose.from(habbitRepository.save(habbit));
	}


}

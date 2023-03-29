package com.clover.habbittracker.domain.habbit.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.habbit.dto.HabbitRequest;
import com.clover.habbittracker.domain.habbit.dto.HabbitRespose;
import com.clover.habbittracker.domain.habbit.entity.Habbit;
import com.clover.habbittracker.domain.habbit.repository.HabbitRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;

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

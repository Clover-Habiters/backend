package com.clover.habbittracker.domain.vote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.vote.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;

}

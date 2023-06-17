package com.clover.habbittracker.domain.like.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.like.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
}

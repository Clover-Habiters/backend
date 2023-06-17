package com.clover.habbittracker.domain.post.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;


}

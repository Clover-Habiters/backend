package com.clover.habbittracker.domain.comment.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
}

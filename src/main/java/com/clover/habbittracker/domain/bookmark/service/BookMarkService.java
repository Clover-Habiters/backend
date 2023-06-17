package com.clover.habbittracker.domain.bookmark.service;

import org.springframework.stereotype.Service;

import com.clover.habbittracker.domain.bookmark.repository.BookMarkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookMarkService {

	private final BookMarkRepository bookMarkRepository;
}

package com.clover.habbittracker.domain.bookmark.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.bookmark.service.BookMarkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookMarkController {

	private final BookMarkService bookMarkService;

}

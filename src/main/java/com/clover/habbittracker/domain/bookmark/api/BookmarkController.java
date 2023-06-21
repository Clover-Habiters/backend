package com.clover.habbittracker.domain.bookmark.api;

import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clover.habbittracker.domain.bookmark.dto.CreateBookmarkRequest;
import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.bookmark.service.BookmarkService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

	private final BookmarkService bookMarkService;

	@PostMapping
	public ResponseEntity<Void> createBookmark(
		@AuthenticationPrincipal Long memberId,
		@NotNull @RequestBody CreateBookmarkRequest request
	) {
		Long bookmarkId = bookMarkService.crate(memberId, request);
		URI location = URI.create("/bookmarks/" + bookmarkId);
		return ResponseEntity.created(location).build();
	}

	@PostMapping
	@ResponseStatus(NO_CONTENT)
	public void addPost(
		@AuthenticationPrincipal Long memberId,
		@RequestParam Long postId,
		@RequestParam(required = false, defaultValue = "1") Long bookmarkId // 옳은 방식인지 아직 감이 안온다..RESTful 한가?
	) {
		bookMarkService.addPost(bookmarkId, memberId, postId);
	}

	@GetMapping
	public ResponseEntity<List<Bookmark>> getAllBookmarks(
		@AuthenticationPrincipal Long memberId
	) {
		List<Bookmark> bookmarks = bookMarkService.getAllBookmarks(memberId);
		return ResponseEntity.ok(bookmarks);

	}

	@GetMapping("/{bookmarkId}")
	public ResponseEntity<Bookmark> getBookmark(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long bookmarkId
	) {
		Bookmark bookmark = bookMarkService.getBookmark(bookmarkId, memberId);
		return ResponseEntity.ok(bookmark);
	}

	@DeleteMapping
	@ResponseStatus(NO_CONTENT)
	public void deletePost(
		@AuthenticationPrincipal Long memberId,
		@RequestParam Long postId,
		@RequestParam(required = false) Long bookmarkId
	) {
		bookMarkService.deletePost(bookmarkId, memberId, postId);
	}

	@DeleteMapping("/{bookmarkId}")
	@ResponseStatus(NO_CONTENT)
	public void deleteBookmark(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long bookmarkId
	) {
		bookMarkService.delete(bookmarkId, memberId);
	}

}

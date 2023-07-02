package com.clover.habbittracker.domain.bookmark.api;

import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.clover.habbittracker.domain.bookmark.dto.BookmarkResponse;
import com.clover.habbittracker.domain.bookmark.dto.CreateBookmarkRequest;
import com.clover.habbittracker.domain.bookmark.service.BookmarkService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

	private final BookmarkService bookMarkService;

	@PostMapping
	public ResponseEntity<Void> createBookmark(
		@AuthenticationPrincipal Long memberId,
		@Valid @RequestBody CreateBookmarkRequest request
	) {
		Long bookmarkId = bookMarkService.crate(memberId, request);
		URI location = URI.create("/bookmarks/" + bookmarkId);
		return ResponseEntity.created(location).build();
	}

	@PostMapping("/{bookmarkId}/posts")
	@ResponseStatus(NO_CONTENT)
	public void addPost(
		@AuthenticationPrincipal Long memberId,
		@PathVariable(required = false) Optional<Long> bookmarkId,
		@RequestParam Long postId
	) {
		bookMarkService.addPost(bookmarkId.orElse(1L), memberId, postId);
	}

	@GetMapping
	public ResponseEntity<List<BookmarkResponse>> getAllBookmarks(
		@AuthenticationPrincipal Long memberId
	) {
		List<BookmarkResponse> bookmarks = bookMarkService.getAllBookmarks(memberId);
		return ResponseEntity.ok(bookmarks);
	}

	@GetMapping("/{bookmarkId}")
	public ResponseEntity<BookmarkResponse> getBookmark(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long bookmarkId
	) {
		BookmarkResponse bookmark = bookMarkService.getBookmark(bookmarkId, memberId);
		return ResponseEntity.ok(bookmark);
	}

	@DeleteMapping("/{bookmarkId}")
	@ResponseStatus(NO_CONTENT)
	public void deletePost(
		@AuthenticationPrincipal Long memberId,
		@PathVariable Long bookmarkId,
		@RequestParam Long postId
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

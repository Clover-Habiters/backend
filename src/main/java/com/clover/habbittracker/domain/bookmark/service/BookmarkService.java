package com.clover.habbittracker.domain.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.bookmark.dto.CreateBookmarkRequest;
import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.bookmark.repository.BookmarkRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookMarkRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	@Transactional
	public Long crate(Long memberId, CreateBookmarkRequest request) {

		Member member = getMemberBy(memberId);
		Bookmark bookmark = new Bookmark(member, request.title(), request.description());
		Bookmark savedBookmark = bookMarkRepository.save(bookmark);

		return savedBookmark.getId();
	}

	@Transactional
	public void addPost(Long bookmarkId, Long memberId, Long postId) {

		Member member = getMemberBy(memberId);
		Post post = getPostBy(postId);

		Bookmark bookmark = bookMarkRepository.findById(bookmarkId)
			.orElseGet(() -> Bookmark.defaultBookmark(member));

		bookmark.addPost(post);
	}

	public List<Bookmark> getAllBookmarks(Long memberId) {
		return bookMarkRepository.findByMemberId(memberId);
	}

	public Bookmark getBookmark(Long bookmarkId, Long memberId) {
		return bookMarkRepository.findByIdAndMemberId(bookmarkId, memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
	}

	public void delete(Long bookmarkId, Long memberId) {
		bookMarkRepository.deleteByIdAndMemberId(bookmarkId, memberId);
	}

	public void deletePost(Long bookmarkId, Long memberId, Long postId) {

		Bookmark bookmark = bookMarkRepository.findByIdAndMemberId(bookmarkId, memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 북마크입니다."));

		bookmark.removePostBy(postId);
	}

	private Member getMemberBy(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
	}

	private Post getPostBy(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
	}

}

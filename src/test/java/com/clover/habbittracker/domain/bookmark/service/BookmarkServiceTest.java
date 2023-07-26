package com.clover.habbittracker.domain.bookmark.service;

import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.bookmark.dto.BookmarkResponse;
import com.clover.habbittracker.domain.bookmark.dto.CreateBookmarkRequest;
import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.bookmark.repository.BookmarkRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

@SpringBootTest
@Transactional
class BookmarkServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	private Member savedMember;

	private Post savedPost;

	@BeforeEach
	void setUp() {
		Member member = createTestMember();
		savedMember = memberRepository.save(member);

		savedPost = postRepository.save(createTestPost(savedMember));
	}

	@Test
	@DisplayName("[성공] 북마크 생성")
	void crate() {
		// given
		CreateBookmarkRequest request = new CreateBookmarkRequest("생성 테스트입니다.", "생성 테스트용입니다.");

		// when
		Long bookmarkId = bookmarkService.crate(savedMember.getId(), request);

		// then
		Optional<Bookmark> saveBookmark = bookmarkRepository.findById(bookmarkId);
		assertThat(saveBookmark).isPresent();
		assertThat(saveBookmark.get().getTitle()).isEqualTo(request.title());
		assertThat(saveBookmark.get().getDescription()).isEqualTo(request.description());
	}

	@Test
	@DisplayName("[성공] 북마크에 게시글 추가")
	void addPost() {
		// given
		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);
		Bookmark saveBookmark = bookmarkRepository.save(bookmark);

		// when
		bookmarkService.addPost(saveBookmark.getId(), savedMember.getId(), savedPost.getId());

		// then
		Optional<Bookmark> savedBookmark = bookmarkRepository.findById(saveBookmark.getId());
		assertThat(savedBookmark).isPresent();
		assertThat(savedBookmark.get().getPosts()).hasSize(1);
		assertThat(savedBookmark.get().getPosts()).contains(savedPost);
	}

	@Test
	@DisplayName("[성공] 북마크 전체 목록 조회")
	void getAllBookmarks() {
		// given
		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		Bookmark bookmark2 = new Bookmark(savedMember, "테스트2입니다.", "테스트용2입니다.");
		bookmark.addPost(savedPost);
		bookmarkRepository.save(bookmark);
		bookmarkRepository.save(bookmark2);

		// when
		List<BookmarkResponse> allBookmarks = bookmarkService.getAllBookmarks(savedMember.getId());

		// then
		assertThat(allBookmarks).hasSize(2);
		assertThat(allBookmarks.get(0).title()).isEqualTo(bookmark.getTitle());
		assertThat(allBookmarks.get(1).description()).isEqualTo(bookmark2.getDescription());
	}

	@Test
	@DisplayName("[성공] 북마크 조회")
	void getBookmark() {
		// given
		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);
		bookmarkRepository.save(bookmark);

		// when
		BookmarkResponse savedBookmark = bookmarkService.getBookmark(bookmark.getId(), savedMember.getId());

		// then
		assertThat(savedBookmark)
			.hasFieldOrPropertyWithValue("title", bookmark.getTitle())
			.hasFieldOrPropertyWithValue("description", bookmark.getDescription());
	}

	@Test
	@DisplayName("[성공] 북마크 삭제")
	void delete() { // soft delete 테스트 필요함
		// given
		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);
		Bookmark saveBookmark = bookmarkRepository.save(bookmark);

		// when
		bookmarkService.delete(saveBookmark.getId(), savedMember.getId());

		// then
		List<Bookmark> savedBookmark = bookmarkRepository.findByMemberId(savedMember.getId());
		assertThat(savedBookmark).isEmpty();
	}

	@Test
	@DisplayName("[성공] 북마크에서 게시글 삭제")
	void deletePost() {
		// given
		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);

		Bookmark saveBookmark = bookmarkRepository.save(bookmark);

		// when
		bookmarkService.deletePost(bookmark.getId(), savedMember.getId(), savedPost.getId());

		// then
		Optional<Bookmark> savedBookmark = bookmarkRepository.findById(saveBookmark.getId());
		assertThat(savedBookmark).isPresent();
		assertThat(savedBookmark.get().getPosts()).isEmpty();
	}
}
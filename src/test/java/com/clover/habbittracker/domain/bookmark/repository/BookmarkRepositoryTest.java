package com.clover.habbittracker.domain.bookmark.repository;

import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;
import com.clover.habbittracker.global.config.db.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookmarkRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	private Member savedMember;

	private Post savedPost;

	private Bookmark saveBookmark;

	@BeforeEach
	void setUp() {
		Member member = createTestMember();
		savedMember = memberRepository.save(member);
		savedPost = postRepository.save(createTestPost(savedMember));

		Bookmark bookmark = new Bookmark(savedMember, "테스트입니다.", "테스트용입니다.");
		bookmark.addPost(savedPost);
		saveBookmark = bookmarkRepository.save(bookmark);
	}

	@Test
	@DisplayName("[성공] 멤버 아이디로 북마크 전체 조회")
	void findByMemberId() {
		//when
		List<Bookmark> byMemberId = bookmarkRepository.findByMemberId(savedMember.getId());
		//then
		assertThat(byMemberId).isNotEmpty();
		assertThat(byMemberId.get(0).getTitle()).isEqualTo(saveBookmark.getTitle());
		assertThat(byMemberId.get(0).getDescription()).isEqualTo(saveBookmark.getDescription());
	}

	@Test
	@DisplayName("[성공] 북마크 아이디와 멤버 아이디로 북마크 조회")
	void findByIdAndMemberId() {
		//when
		Optional<Bookmark> byIdAndMemberId
			= bookmarkRepository.findByIdAndMemberId(saveBookmark.getId(), savedMember.getId());

		//then
		assertThat(byIdAndMemberId).isPresent();
		assertThat(byIdAndMemberId.get().getTitle()).isEqualTo(saveBookmark.getTitle());
		assertThat(byIdAndMemberId.get().getDescription()).isEqualTo(saveBookmark.getDescription());
	}

	@Test
	@DisplayName("[성공] 북마크 아이디와 멤버 아이디로 북마크 삭제")
	void deleteByIdAndMemberId() {
		// when
		bookmarkRepository.deleteByIdAndMemberId(saveBookmark.getId(), savedMember.getId());

		//then
		Optional<Bookmark> byIdAndMemberId
			= bookmarkRepository.findByIdAndMemberId(saveBookmark.getId(), savedMember.getId());

		assertThat(byIdAndMemberId).isNotPresent();
	}

}
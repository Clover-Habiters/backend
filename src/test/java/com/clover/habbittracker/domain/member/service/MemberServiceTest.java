package com.clover.habbittracker.domain.member.service;

import static com.clover.habbittracker.util.CommentProvider.*;
import static com.clover.habbittracker.util.MemberProvider.*;
import static com.clover.habbittracker.util.PostProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.bookmark.entity.Bookmark;
import com.clover.habbittracker.domain.bookmark.repository.BookmarkRepository;
import com.clover.habbittracker.domain.comment.repository.CommentRepository;
import com.clover.habbittracker.domain.member.dto.MemberReportResponse;
import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.exception.MemberDuplicateNickName;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.domain.post.repository.PostRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	private Member savedMember;

	@BeforeEach
	void setUp() {
		savedMember = memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("사용자 ID로 사용자 프로필 정보를 얻어 올 수 있다.")
	void successGetProfileTest() {
		//when
		MemberResponse memberResponse = memberService.getProfile(savedMember.getId());
		//then
		assertThat(memberResponse.getNickName()).isEqualTo(getNickName());
	}

	@Test
	@DisplayName("잘못된 사용자 ID로 사용자 프로필을 조회 할 경우 예외가 터진다.")
	void failedGetProfileTest() {
		assertThrows(MemberNotFoundException.class, () -> {
			memberService.getProfile(-1L);
		});
	}

	@Test
	@DisplayName("사용자에게 nickName 요청받아, 사용자 프로필을 업데이트 할 수 있다.")
	void successUpdateProfileTest() {

		//given
		MemberRequest memberRequest = new MemberRequest("updateNickName");

		//when
		MemberResponse memberResponse = memberService.updateProfile(savedMember.getId(), memberRequest);

		//then
		assertThat(memberResponse)
			.hasFieldOrPropertyWithValue("nickName", memberRequest.getNickName());
	}

	@Test
	@DisplayName("중복된 닉네임은 사용 할 수 없습니다.")
	void failedUpdateProfileTest() {

		//given
		MemberRequest memberRequest = new MemberRequest("testNickName");

		//when then
		assertThrows(MemberDuplicateNickName.class, () -> {
			memberService.updateProfile(savedMember.getId(), memberRequest);
		});
	}

	@Test
	@DisplayName("사용자는 자신의 요악된 정보를 조회 할 수 있다.")
	void getMyReport() {
		//givne
		Post savedPost = postRepository.save(createTestPost(savedMember));
		commentRepository.save(createTestComment(savedMember, savedPost));
		bookmarkRepository.save(new Bookmark(savedMember, "testTitle", "testDescription"));
		//when
		MemberReportResponse myReport = memberService.getMyReport(savedMember.getId());
		//then
		assertThat(myReport.numOfPost()).isEqualTo(1);
		assertThat(myReport.numOfComment()).isEqualTo(1);
		assertThat(myReport.numOfBookmark()).isEqualTo(1);
	}
}

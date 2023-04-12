package com.clover.habbittracker.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.oauth.dto.GoogleUser;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb", "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa", "spring.datasource.password=" })
public class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	private final Long SAVED_ID = 1L;

	private final String SAVED_NICK_NAME ="testNickName";

	@BeforeEach
	void setUp() {
		Member testMember = Member.builder()
			.id(SAVED_ID)
			.email("test@email.com")
			.profileImgUrl("testImgUrl")
			.nickName("testNickName")
			.oauthId("testOauthId")
			.provider("testProvider")
			.build();
		memberRepository.save(testMember);
	}


	@Test
	@DisplayName("사용자 ID로 사용자 프로필 정보를 얻어 올 수 있다.")
	void successGetProfileTest() {
		//when
		MemberResponse memberResponse = memberService.getProfile(SAVED_ID);
		//then
		assertThat(memberResponse.getNickName()).isEqualTo(SAVED_NICK_NAME);
	}

	@Test
	@DisplayName("잘못된 사용자 ID로 사용자 프로필을 조회 할 경우 예외가 터진다.")
	void failedGetProfileTest() {
		assertThrows(NoSuchElementException.class, () -> {
			memberService.getProfile(2L);
		});
	}

	@Test
	@DisplayName("사용자에게 nickName,ProfileImgUrl을 요청받아, 사용자 프로필을 업데이트 할 수 있다.")
	void successUpdateProfileTest() {

		//given
		MemberRequest memberRequest = MemberRequest.builder()
			.nickName("updateNickName")
			.profileImgUrl("updateImgUrl")
			.build();

		//when
		MemberResponse memberResponse = memberService.updateProfile(SAVED_ID, memberRequest);

		//then
		assertThat(memberResponse)
			.hasFieldOrPropertyWithValue("nickName", memberRequest.getNickName())
			.hasFieldOrPropertyWithValue("profileImgUrl", memberRequest.getProfileImgUrl());
	}

	@Test
	@DisplayName("사용자에게 nickName,ProfileImgUrl 하나만 요청 받아 사용자 프로필을 업데이트 할 수 있다.")
	void optionUpdateProfileTest() {
		//given
		MemberRequest memberRequest = MemberRequest.builder().profileImgUrl("updateImgUrl2").build();

		//when
		MemberResponse memberResponse = memberService.updateProfile(1L, memberRequest);

		//then
		// 닉네임은 변경되고, 그 외의는 이전의 데이터와 같은지 비교
		assertThat(memberResponse)
			.hasFieldOrPropertyWithValue("profileImgUrl", memberRequest.getProfileImgUrl())
			.hasFieldOrPropertyWithValue("nickName",SAVED_NICK_NAME);
	}

	@Test
	@DisplayName("로그인을 할 경우 oauthId와 provider를 비교하여 사용자 정보가 없다면 자동으로 회원가입 한다.")
	void memberRegisterTest() {
		//given
		SocialUser newUser = GoogleUser.builder()
			.oauthId("GoogleOauthId")
			.provider("google")
			.build();
		SocialUser savedUser = GoogleUser.builder()
			.oauthId("testOauthId")
			.provider("testProvider")
			.build();
		//when
		Long newMemberID = memberService.join(newUser);
		Long savedUserId = memberService.join(savedUser);
		//then
		assertThat(newMemberID).isNotSameAs(SAVED_ID);
		assertThat(savedUserId).isEqualTo(SAVED_ID);
	}


}

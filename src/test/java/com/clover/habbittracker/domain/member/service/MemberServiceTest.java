package com.clover.habbittracker.domain.member.service;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.oauth.dto.GoogleUser;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb", "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa", "spring.datasource.password=" })
public class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		memberRepository.save(createTestMember());
	}


	@Test
	@DisplayName("사용자 ID로 사용자 프로필 정보를 얻어 올 수 있다.")
	void successGetProfileTest() {
		//when
		MemberResponse memberResponse = memberService.getProfile(getId());
		//then
		assertThat(memberResponse.getNickName()).isEqualTo(getNickName());
	}

	@Test
	@DisplayName("잘못된 사용자 ID로 사용자 프로필을 조회 할 경우 예외가 터진다.")
	void failedGetProfileTest() {
		assertThrows(MemberNotFoundException.class, () -> {
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
		MemberResponse memberResponse = memberService.updateProfile(getId(), memberRequest);

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
			.hasFieldOrPropertyWithValue("nickName",getNickName());
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
		assertThat(newMemberID).isNotSameAs(getId());
		assertThat(savedUserId).isEqualTo(getId());
	}


}

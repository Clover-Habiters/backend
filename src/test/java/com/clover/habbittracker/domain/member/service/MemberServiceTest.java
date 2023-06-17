package com.clover.habbittracker.domain.member.service;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.dto.MemberResponse;
import com.clover.habbittracker.domain.member.exception.MemberDuplicateNickName;
import com.clover.habbittracker.domain.member.exception.MemberNotFoundException;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.oauth.dto.GoogleUser;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	private Long saveId;

	@BeforeEach
	void setUp() {
		saveId = memberRepository.save(createTestMember()).getId();
	}


	@Test
	@DisplayName("사용자 ID로 사용자 프로필 정보를 얻어 올 수 있다.")
	void successGetProfileTest() {
		//when
		MemberResponse memberResponse = memberService.getProfile(saveId);
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
		MemberResponse memberResponse = memberService.updateProfile(saveId, memberRequest);

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
			memberService.updateProfile(saveId, memberRequest);
		});
	}

	@Test
	@DisplayName("로그인을 할 경우 oauthId와 provider를 비교하여 사용자 정보가 없다면 자동으로 회원가입 한다.")
	void memberRegisterTest() {
		//given
		SocialUser newUser = GoogleUser.builder()
			.oauthId("GoogleOauthId")
			.provider("google")
			.nickName(getNickName())
			.build();
		SocialUser savedUser = GoogleUser.builder()
			.oauthId("testOauthId")
			.provider("testProvider")
			.nickName(getNickName())
			.build();
		//when
		Long newMemberID = memberService.join(newUser);
		Long savedUserId = memberService.join(savedUser);
		//then
		assertThat(newMemberID).isNotSameAs(saveId);
		assertThat(savedUserId).isEqualTo(saveId);
	}

	@Test
	@DisplayName("회원 가입을 할 경우 닉네임이 8자 이상이라면, \"해비터_\"를 공백을 제외한 8자까지 닉네임을 사용한다")
	void memberRegisterTrimNickNameTest() {
		//given
		SocialUser newUser = GoogleUser.builder()
			.oauthId("GoogleOauthId")
			.provider("google")
			.nickName("test Nick Name")
			.build();
		Long newMemberID = memberService.join(newUser);
		//when
		MemberResponse profile = memberService.getProfile(newMemberID);
		//then
		assertThat(profile.getNickName().substring(4)).isEqualTo(newUser.getNickName().replace(" ","").substring(0,8));

	}

}

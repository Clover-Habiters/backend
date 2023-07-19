package com.clover.habbittracker.global.security.oauth;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.clover.habbittracker.global.security.oauth.dto.SocialUser;

import io.jsonwebtoken.Claims;

@SpringBootTest
public class OauthServiceTest {

	@Autowired
	private OauthService oauthService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;

	@BeforeEach
	void setUp() {
		testMember = createTestMember();
	}

	@Test
	@DisplayName("로그인을 할 경우 oauthId와 provider를 비교하여 사용자 정보가 없다면 자동으로 회원가입 한다.")
	void memberRegisterTest() {
		//given
		SocialUser user = SocialUser.builder()
			.oauthId("GoogleOauthId")
			.provider("google")
			.nickName(testMember.getNickName())
			.build();

		//when
		String accessToken = oauthService.login(user);
		//then
		Claims claims = jwtProvider.getClaims(accessToken);
		Long memberId = claims.get("userId", Long.class);
		Optional<Member> savedMember = memberRepository.findById(memberId);

		assertThat(savedMember).isPresent();
		assertThat(savedMember.get().getId()).isEqualTo(memberId);
	}
}

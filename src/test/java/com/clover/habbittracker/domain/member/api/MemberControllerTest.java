package com.clover.habbittracker.domain.member.api;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb", "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa", "spring.datasource.password=" })
@AutoConfigureMockMvc
public class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private MemberRepository memberRepository;

	private String accessJwt;

	@BeforeEach
	@Transactional
	void setUp() {
		memberRepository.save(createTestMember());
		accessJwt = jwtProvider.createAccessJwt(1L);
	}


	@Test
	@DisplayName("사용자는 자신의 프로필 정보를 조회할수있다.")
	void getMyProfileTest() throws Exception {
		//when then
		mockMvc.perform(get("/users/me").header("Authorization","Bearer " + accessJwt))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.email").exists())
			.andExpect(jsonPath("$.nickName").exists())
			.andExpect(jsonPath("$.profileImgUrl").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("사용자는 자신의 프로필 닉네임과 프로필 이미지를 바꿀수있다.")
	void updateMyProfileTest() throws Exception {
		//given
		MemberRequest memberRequest = MemberRequest.builder()
			.nickName("updateNickName")
			.profileImgUrl("updateProfileImgUrl")
			.build();
		String request = new ObjectMapper().writeValueAsString(memberRequest);

		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization","Bearer " + accessJwt)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickName",is("updateNickName")))
			.andExpect(jsonPath("$.profileImgUrl",is("updateProfileImgUrl")))
			.andDo(print());
	}
	@Test
	@DisplayName("기존의 사용하고 있는 닉네임은 중복으로 생각하여 예외가 발생한다.")
	void updateNickNameDuplicateTest() throws Exception {
		//given
		MemberRequest memberRequest = MemberRequest.builder()
			.nickName("testNickName")
			.build();
		String request = new ObjectMapper().writeValueAsString(memberRequest);

		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization", "Bearer " + accessJwt)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IllegalArgumentException.class));
	}


	@Test
	@DisplayName("사용자는 자신의 프로필 정보를 삭제 할 수 있다.")
	void deleteProfileTest() throws Exception {
		//given
		Member soonDeleteMember = Member.builder()
			.id(2L)
			.email("test@email.com")
			.profileImgUrl("delete")
			.nickName("delete")
			.oauthId("delete")
			.provider("delete")
			.build();
		memberRepository.save(soonDeleteMember);
		String deleteMemberToken = jwtProvider.createAccessJwt(2L);

		//when then
		mockMvc.perform(delete("/users/me").header("Authorization", "Bearer " + deleteMemberToken))
			.andExpect(status().isNoContent())
			.andDo(print());
	}
}

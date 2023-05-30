package com.clover.habbittracker.domain.member.api;

import static com.clover.habbittracker.global.util.ApiDocumentUtils.*;
import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
	"spring.datasource.password="})
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.habiters.store")
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
		mockMvc.perform(get("/users/me")
				.header("Authorization", "Bearer " + accessJwt))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.email").exists())
			.andExpect(jsonPath("$.data.nickName").exists())
			.andExpect(jsonPath("$.data.profileImgUrl").exists())
			.andDo(document("member-read",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("회원 아이디"),
					fieldWithPath("data.email").type(STRING).description("회원 이메일"),
					fieldWithPath("data.nickName").type(STRING).description("회원 닉네임"),
					fieldWithPath("data.profileImgUrl").type(STRING).description("회원 프로필 사진 URL")
				)
			));
	}
	@Test
	@DisplayName("기존의 사용하고 있는 닉네임은 중복으로 생각하여 예외가 발생한다.")
	void updateNickNameDuplicateTest() throws Exception {
		//given
		String request = new ObjectMapper().writeValueAsString(new MemberRequest("testNickName"));

		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization", "Bearer " + accessJwt)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(
				result -> assertThat(result.getResolvedException()).isInstanceOf(IllegalArgumentException.class))
			.andDo(document("duplicate-member",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("errorName").type(STRING).description("결과 코드"),
					fieldWithPath("msg").type(STRING).description("결과 메시지")
				)));
	}
	@Test
	@DisplayName("사용자는 자신의 프로필 닉네임만 변경할 수 있다.")
	void updateMyProfileNickNameTest() throws Exception {
		//given
		String request = new ObjectMapper().writeValueAsString(new MemberRequest("updateNickName"));

		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization", "Bearer " + accessJwt)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.nickName", is("updateNickName")))
			.andDo(document("member-update-nickName",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("nickName").description("수정할 닉네임")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지"),
					fieldWithPath("data.id").type(NUMBER).description("회원 아이디"),
					fieldWithPath("data.email").type(STRING).description("회원 이메일"),
					fieldWithPath("data.nickName").type(STRING).description("회원 닉네임"),
					fieldWithPath("data.profileImgUrl").type(STRING).description("회원 프로필 사진 URL")
				)
			));
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
			.andDo(document("member-delete",
				getDocumentRequest(),
				getDocumentResponse(),
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				responseFields(
					fieldWithPath("code").type(STRING).description("결과 코드"),
					fieldWithPath("message").type(STRING).description("결과 메시지")
				)
			));
	}
}

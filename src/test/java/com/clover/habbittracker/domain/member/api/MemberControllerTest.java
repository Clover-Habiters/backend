package com.clover.habbittracker.domain.member.api;

import static com.clover.habbittracker.global.restdocs.config.RestDocsConfig.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.base.RestDocsSupport;
import com.clover.habbittracker.domain.member.dto.MemberRequest;
import com.clover.habbittracker.domain.member.exception.MemberDuplicateNickName;

public class MemberControllerTest extends RestDocsSupport {

	@Test
	@DisplayName("사용자는 자신의 프로필 정보를 조회할수있다.")
	void getMyProfileTest() throws Exception {
		//when then
		mockMvc.perform(get("/users/me")
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
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
		String request = objectMapper.writeValueAsString(new MemberRequest("testNickName"));
		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(
				result -> assertThat(result.getResolvedException()).isInstanceOf(MemberDuplicateNickName.class))
			.andDo(restDocs.document(
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
		String request = objectMapper.writeValueAsString(new MemberRequest("updateNick"));

		//when then
		mockMvc.perform(put("/users/me")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName("Authorization").description("JWT Access 토큰")
				),
				requestFields(
					fieldWithPath("nickName").description("수정할 닉네임").attributes(field("constraints", "15자 이내"))
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

		//when then
		mockMvc.perform(delete("/users/me").header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
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

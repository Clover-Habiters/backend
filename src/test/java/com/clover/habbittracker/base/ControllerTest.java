package com.clover.habbittracker.base;

import static com.clover.habbittracker.util.MemberProvider.*;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.auth.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.habiters.store")
public abstract class ControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected JwtProvider jwtProvider;

	@Autowired
	protected MemberRepository memberRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	protected Member savedMember;

	protected String accessToken;
	protected final Logger logger = LoggerFactory.getLogger("controllerLog");

	@BeforeEach
	void setUpBase() {
		Member member = createTestMember();
		savedMember = memberRepository.save(member);
		accessToken = jwtProvider.createAccessJwt(savedMember.getId());
		logger.debug("Base Setting Complete");
	}
}

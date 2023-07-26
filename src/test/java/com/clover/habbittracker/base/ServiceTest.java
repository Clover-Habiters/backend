package com.clover.habbittracker.base;

import static com.clover.habbittracker.util.MemberProvider.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

	@Autowired
	protected MemberRepository memberRepository;

	protected Member savedMember;

	@BeforeEach
	void setUpBase() {
		Member member = createTestMember();
		savedMember = memberRepository.save(member);
		System.out.println("Base Setting Complete");
	}
}

package com.clover.habbittracker.base;

import static com.clover.habbittracker.util.MemberProvider.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.config.db.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class RepositoryTest {

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

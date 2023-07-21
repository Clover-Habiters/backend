package com.clover.habbittracker.domain.member.repository;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.global.config.db.JpaConfig;

@DataJpaTest(properties = {"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
	"spring.datasource.password="})
@Import(JpaConfig.class)
public class MemberRepositoryTest {

	private final MemberRepository memberRepository;

	@Autowired
	public MemberRepositoryTest(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@BeforeEach
	void setMemberData() {
		memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("사용자를 저장 할 수 있다.")
	void saveMember() {
		//given
		//when
		//then
		assertThat(getId()).isNotNull();
	}

	@Test
	@DisplayName("사용자의 id로 저장된 사용자 정보를 조회할 수 있다.")
	void MemberFindById() {
		//given
		Member testMember2 = memberRepository.save(createTestMember(2L));
		//when
		Optional<Member> savedMember = memberRepository.findById(testMember2.getId());

		//then
		assertThat(savedMember).isPresent();
		assertThat(savedMember.get()).usingRecursiveComparison().isEqualTo(testMember2);
		assertThat(savedMember.get()).isEqualTo(testMember2);
	}

	@Test
	@DisplayName("저장되지 않는 사용자를 조회 할 경우 Optional.empty 가 반환된다.")
	void emptyMemberFindById() {
		//given
		Long emptyMemberId = 10L;
		//when
		Optional<Member> memberFromFindById = memberRepository.findById(emptyMemberId);
		//then
		assertThat(memberFromFindById).isEmpty();
	}

	@Test
	@DisplayName("사용자 ID로 저장된 사용자 정보를 삭제할 수 있다.")
	void deleteMember() {
		//when
		memberRepository.deleteById(getId());
		Optional<Member> deleteMember = memberRepository.findById(getId());
		//then
		assertThat(deleteMember).isEmpty();
	}
}

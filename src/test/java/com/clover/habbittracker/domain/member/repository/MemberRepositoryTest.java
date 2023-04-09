package com.clover.habbittracker.domain.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.global.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public class MemberRepositoryTest {

	private final MemberRepository memberRepository;

	private Member testMember;

	@Autowired
	public MemberRepositoryTest(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@BeforeEach
	void setMemberData() {
		// ID 자동 증가를 확인 하기 위해 ID는 제외하고 멤버 객체 생성
		testMember = Member.builder()
			.email("test@email.com")
			.profileImgUrl("testImgUrl")
			.nickName("testNickName")
			.oauthId("testOauthId")
			.provider("testProvider")
			.build();
	}


	@Test
	@DisplayName("사용자를 저장 할 수 있으며, ID는 DBMS가 관리한다.")
	void saveMember() {
		//given
		//when
		Member saveMember = memberRepository.save(testMember);
		//then
		assertThat(saveMember.getId()).isNotNull();
	}

	@Test
	@DisplayName("사용자의 id로 저장된 사용자 정보를 조회할 수 있다.")
	void MemberFindById() {
		//given
		Member saveMember = memberRepository.save(testMember);

		//when
		Optional<Member> memberFromFindById = memberRepository.findById(saveMember.getId());

		//then
		assertThat(memberFromFindById).isPresent();
		assertThat(memberFromFindById.get()).usingRecursiveComparison().isEqualTo(saveMember);
		assertThat(memberFromFindById.get()).isEqualTo(saveMember);
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
		//given
		Member saveMember = memberRepository.save(testMember);
		Long id = saveMember.getId();
		//when
		memberRepository.deleteById(id);
		Optional<Member> deleteMember = memberRepository.findById(id);
		//then
		assertThat(deleteMember).isEmpty();
	}
}

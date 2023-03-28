package com.clover.habbittracker.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query("""
		SELECT u
		FROM Member u
		WHERE u.provider = :provider
		and u.oauthId = :oauthId
		""")
	Optional<Member> findByProviderAndOauthId(
		@Param("provider") String provider,
		@Param("oauthId") String oauthId);

	Optional<Member> findByNickName(String nickName);
}

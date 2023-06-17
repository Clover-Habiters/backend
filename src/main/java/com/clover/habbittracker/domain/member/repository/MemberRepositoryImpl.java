package com.clover.habbittracker.domain.member.repository;

import static com.clover.habbittracker.domain.member.entity.QMember.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Member> findByProviderAndOauthId(String provider, String oauthId) {

		Member result = jpaQueryFactory.selectFrom(member)
			.where(member.provider.eq(provider).and(member.oauthId.eq(oauthId)))
			.fetchOne();

		return Optional.ofNullable(result);

	}

	@Override
	public Optional<Member> findByNickName(String nickName) {

		Member result = jpaQueryFactory.selectFrom(member)
			.where(member.nickName.eq(nickName))
			.fetchOne();

		return Optional.ofNullable(result);
	}


}

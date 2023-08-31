package com.clover.habbittracker.domain.member.repository;

import static com.clover.habbittracker.domain.member.entity.QMember.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clover.habbittracker.domain.member.dto.MemberReportResponse;
import com.clover.habbittracker.domain.member.dto.QMemberReportResponse;
import com.clover.habbittracker.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

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

	@Override
	public MemberReportResponse findReportById(Long memberId) {
		return jpaQueryFactory
			.select(QMemberReportResponse())
			.from(member)
			.where(member.id.eq(memberId))
			.fetchOne();
	}

	private QMemberReportResponse QMemberReportResponse() {
		return new QMemberReportResponse(
			member.posts.size(),
			member.comments.size(),
			member.bookmarks.size()
		);
	}
}

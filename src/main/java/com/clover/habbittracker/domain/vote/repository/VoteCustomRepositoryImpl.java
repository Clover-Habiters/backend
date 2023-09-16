package com.clover.habbittracker.domain.vote.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VoteCustomRepositoryImpl implements VoteCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

}

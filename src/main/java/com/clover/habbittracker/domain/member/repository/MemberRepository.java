package com.clover.habbittracker.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.habbittracker.domain.member.entity.Member;

public interface MemberRepository extends MemberCustomRepository, JpaRepository<Member, Long> {

}

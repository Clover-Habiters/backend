package com.clover.habbittracker.domain.emoji.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.emoji.entity.Emoji;

public interface EmojiRepository extends JpaRepository<Emoji, Long>, EmojiCustomRepository {

	@Query(value = "select * from member "
	               + "where memberId = :memberId "
	               + "AND domainId = :memberId "
	               + "AND domain = 'POST'",
		nativeQuery = true)
	Optional<Emoji> findByMemberIdAndPostId(
		@Param("memberId") Long memberId,
		@Param("postId") Long postId
	);

	@Query(value = "select * from member "
	               + "where memberId = :memberId "
	               + "AND domainId = :commentId "
	               + "AND domain = 'COMMENT'",
		nativeQuery = true)
	Optional<Emoji> findByMemberIdAndCommentId(
		@Param("memberId") Long memberId,
		@Param("commentId") Long commentId
	);
}


package com.clover.habbittracker.domain.diary.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
	@Query("""
			SELECT d
			FROM Diary d
			WHERE d.member.id = :memberId
			AND d.createdAt BETWEEN :start AND :end
		""")
	List<Diary> findByMemberId(@Param("memberId") Long memberId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}

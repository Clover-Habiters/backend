package com.clover.habbittracker.domain.habit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.habit.entity.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long> {
	@Query("""
		SELECT h
		FROM Habit h
		LEFT JOIN FETCH h.habitChecks
		WHERE h.member.id = :memberId
		AND h.createdAt BETWEEN :start AND :end
		""")
	List<Habit> joinHabitCheckFindByMemberId(@Param("memberId") Long memberId,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end);
}

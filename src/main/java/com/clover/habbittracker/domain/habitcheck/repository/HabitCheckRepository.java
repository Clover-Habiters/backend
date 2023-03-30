package com.clover.habbittracker.domain.habitcheck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;

public interface HabitCheckRepository extends JpaRepository<HabitCheck,Long> {

	@Query("""
		SELECT hc
		FROM HabitCheck hc
		WHERE hc.updatedAt = (SELECT MAX(hc.updatedAt) FROM HabitCheck hc)
		AND hc.habit = :habit
""")
	Optional<HabitCheck> findByHabitOrderByUpdatedAtDesc(@Param("habit") Habit habit);

}

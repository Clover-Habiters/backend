package com.clover.habbittracker.domain.habitCheck.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habit.repository.HabitRepository;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.habitcheck.repository.HabitCheckRepository;
import com.clover.habbittracker.global.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public class HabitCheckRepositoryTest {

	@Autowired
	private HabitCheckRepository habitCheckRepository;

	@Autowired
	private HabitRepository habitRepository;

	@Test
	@DisplayName("습관 체크를 저장할 수 있다.")
	void saveHabitCheckTest() {

		//given
		Habit testHabit = Habit.builder().content("테스트 습관입니다.").build();
		HabitCheck testHabitCheck = HabitCheck.builder().checked(true).habit(testHabit).build();

		//when
		HabitCheck habitCheck = habitCheckRepository.save(testHabitCheck);

		//then
		assertThat(habitCheck.getHabit()).isEqualTo(testHabit);
	}


	@Test
	@DisplayName("습관정보로 습관 체크 내역을 조회 할 수 있다.")
	void findByHabitTest() {
		//given
		Habit testHabit = Habit.builder().content("테스트 습관입니다.").build();
		habitRepository.save(testHabit);
		habitCheckRepository.save(HabitCheck.builder().checked(true).habit(testHabit).build());

		//when
		Optional<HabitCheck> optionalHabitCheck = habitCheckRepository.findByHabit(testHabit);

		//then
		assertThat(optionalHabitCheck).isPresent();
		assertThat(optionalHabitCheck.get().getHabit()).isEqualTo(testHabit);
	}

	@Test
	@DisplayName("습관 정보로 가장 최근에 체크된 내역을 조회 할 수 있다.")
	void findByHabitOrderByDescTest() {
		//given
		Habit testHabit = Habit.builder().content("테스트 습관입니다.").build();
		habitRepository.save(testHabit);
		for (int i = 0; i < 10; i++) {
			habitCheckRepository.save(HabitCheck.builder().checked(true).habit(testHabit).build());
		}

		//when
		Optional<HabitCheck> byHabitOrderByIdDesc = habitCheckRepository.findByHabitOrderByUpdatedAtDesc(testHabit);

		//then
		assertThat(byHabitOrderByIdDesc).isPresent();
		assertThat(byHabitOrderByIdDesc.get().getId()).isGreaterThan(1L);
	}

}

package com.clover.habbittracker.domain.habit.repository;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.habit.entity.Habit;
import com.clover.habbittracker.domain.habitcheck.entity.HabitCheck;
import com.clover.habbittracker.domain.habitcheck.repository.HabitCheckRepository;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public class HabitRepositoryTest {
	private final HabitRepository habitRepository;

	private final MemberRepository memberRepository;

	private final HabitCheckRepository habitCheckRepository;

	private Member testMember;

	@Autowired
	public HabitRepositoryTest(HabitRepository habitRepository, MemberRepository memberRepository,
		HabitCheckRepository habitCheckRepository) {
		this.habitRepository = habitRepository;
		this.memberRepository = memberRepository;
		this.habitCheckRepository = habitCheckRepository;
	}

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("사용자 정보를 통해 사용자 정보와 함께 습관을 저장 할 수 있다.")
	void saveHabitTest() {

		//given
		Habit testHabit = Habit.builder().content("testHabit").member(testMember).build();

		//when
		Habit saveHabit = habitRepository.save(testHabit);

		//then
		assertThat(saveHabit).usingRecursiveComparison().isEqualTo(testHabit);
	}

	@Test
	@DisplayName("습관ID 로 습관 정보를 조회 할 수 있다.")
	void findByIdTest() {

		//given
		Habit testHabit = Habit.builder().content("testHabit").member(testMember).build();
		Habit saveHabit = habitRepository.save(testHabit);

		//when
		Optional<Habit> findHabit = habitRepository.findById(saveHabit.getId());

		//then
		assertThat(findHabit).isPresent();
		assertThat(findHabit.get()).usingRecursiveComparison().isEqualTo(saveHabit);
		assertThat(findHabit.get()).isEqualTo(saveHabit);
	}

	@Test
	@DisplayName("사용자 ID로 습관리스트와 해당 습관 체크리스트를 조회 할 수있다.")
	void joinHabitCheckFindByMemberIdTest() {
		//given
		Habit testHabit = Habit.builder()
			.content("testHabit")
			.member(testMember)
			.build();
		habitRepository.save(testHabit);
		HabitCheck habitCheck = HabitCheck.builder().checked(true).habit(testHabit).build();
		habitCheckRepository.save(habitCheck);

		// h2 in-memory 환경이므로 테스트 시 일대다 관계 저장을 지원 하지않기떄문에 직접 리스트에 추가.
		testHabit.getHabitChecks().add(habitCheck);

		//then
		List<Habit> testMemberHabitList = habitRepository.joinHabitCheckFindByMemberId(testMember.getId());

		//when
		assertThat(testMemberHabitList.size()).isEqualTo(1);
		assertThat(testMemberHabitList.get(0).getHabitChecks().size()).isEqualTo(1);
	}
}

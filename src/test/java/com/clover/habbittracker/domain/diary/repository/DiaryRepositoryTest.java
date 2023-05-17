package com.clover.habbittracker.domain.diary.repository;

import static com.clover.habbittracker.global.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.clover.habbittracker.domain.diary.entity.Diary;
import com.clover.habbittracker.domain.member.entity.Member;
import com.clover.habbittracker.domain.member.repository.MemberRepository;
import com.clover.habbittracker.global.config.JpaConfig;
import com.clover.habbittracker.global.util.DateUtil;

@DataJpaTest
@Import(JpaConfig.class)
public class DiaryRepositoryTest {

	@Autowired
	private DiaryRepository diaryRepository;
	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(createTestMember());
	}

	@Test
	@DisplayName("회고록을 저장 할 수 있다.")
	void saveTest() {
		//given
		Diary testDiary = Diary.builder().content("회고록 테스트입니다.").build();

		//when
		Diary saveDiary = diaryRepository.save(testDiary);

		//then
		assertThat(saveDiary).isEqualTo(testDiary);
		assertThat(saveDiary).usingRecursiveComparison().isEqualTo(testDiary);
	}

	@Test
	@DisplayName("회고록 ID로 회고내용을 조회 할 수 있다.")
	void findByIdTest() {
		//given
		Diary testDiary = Diary.builder().content("회고록 테스트입니다.").build();
		diaryRepository.save(testDiary);

		//when
		Optional<Diary> optionalDiary = diaryRepository.findById(testDiary.getId());

		//then
		assertThat(optionalDiary).isPresent();
		assertThat(optionalDiary.get()).isEqualTo(testDiary);
	}

	@Test
	@DisplayName("회고록 ID로 회고 내용을 삭제 할 수 있다.")
	void deleteByIdTest() {
		//given
		Diary testDiary = Diary.builder().content("회고록 테스트입니다.").build();
		diaryRepository.save(testDiary);

		//when
		diaryRepository.deleteById(testDiary.getId());

		//then
		Optional<Diary> optionalDiary = diaryRepository.findById(testDiary.getId());
		assertThat(optionalDiary).isEmpty();
	}

	@Test
	@DisplayName("사용자의 월별 회고록 리스트를 조회 할 수 있다.")
	void findByMemberIdDateBetweenTest() {
		//given
		for (int i = 0; i < 10; i++) {
			diaryRepository.save(Diary.builder().content("테스트회고입니다." + i).member(testMember).build());
		}
		Map<String, LocalDateTime> dateTimeMap1 = DateUtil.getMonthStartAndEndDate(null);
		Map<String, LocalDateTime> dateTimeMap2 = DateUtil.getMonthStartAndEndDate("2023-03");

		//when
		List<Diary> diaryList1 = diaryRepository.findByMemberId(testMember.getId(), dateTimeMap1.get("start"),
			dateTimeMap1.get("end"));
		List<Diary> diaryList2 = diaryRepository.findByMemberId(testMember.getId(), dateTimeMap2.get("start"),
			dateTimeMap2.get("end"));

		//then
		assertThat(diaryList1.size()).isEqualTo(10);
		assertThat(diaryList2.size()).isEqualTo(0);
		diaryList1.forEach(
			diary -> assertThat(diary)
				.hasFieldOrProperty("id")
				.hasFieldOrProperty("content"));
	}

}

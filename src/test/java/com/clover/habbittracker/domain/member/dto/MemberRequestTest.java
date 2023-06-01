package com.clover.habbittracker.domain.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.domain.habit.dto.HabitRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MemberRequestTest {
	private static Validator validator;

	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("습관 내용이 없을 경우 validation 에러가 발생하며, 에러 메세지를 출력한다.")
	public void diaryValueNullTest() {
		//given
		HabitRequest request = new HabitRequest();

		//when
		Set<ConstraintViolation<HabitRequest>> violations = validator.validate(request);
		ConstraintViolation<HabitRequest> violation = violations.iterator().next();

		//then
		assertThat(violation).isNotNull();
		assertThat(violation.getMessage()).isEqualTo("습관 내용이 비어 있을 수 없습니다.");
	}

	@Test
	@DisplayName("습관 내용이 10자 이상이면 validation 에러가 발생하며, 에러 메세지를 출력한다.")
	void diaryMaxSizeTest() {
		//given
		HabitRequest request = new HabitRequest("a".repeat(11));

		//when
		Set<ConstraintViolation<HabitRequest>> violations = validator.validate(request);
		ConstraintViolation<HabitRequest> violation = violations.iterator().next();

		//then
		assertThat(violation).isNotNull();
		assertThat(violation.getMessage()).isEqualTo("습관은 10글자 이내로 입력해주세요.");

	}
}

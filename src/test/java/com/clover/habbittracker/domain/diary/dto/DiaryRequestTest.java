package com.clover.habbittracker.domain.diary.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class DiaryRequestTest {
	private static Validator validator;

	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("회고록 내용이 없을 경우 validation 에러가 발생하며, 에러 메세지를 출력한다.")
	public void diaryValueNullTest() {
		//given
		DiaryRequest request = new DiaryRequest();

		//when
		Set<ConstraintViolation<DiaryRequest>> violations = validator.validate(request);
		ConstraintViolation<DiaryRequest> violation = violations.iterator().next();

		//then
		assertThat(violation).isNotNull();
		assertThat(violation.getMessage()).isEqualTo("회고록이 비어 있을 수 없습니다.");
	}

	@Test
	@DisplayName("회고록 내용이 200자 이상이면 validation 에러가 발생하며, 에러 메세지를 출력한다.")
	void diaryMaxSizeTest() {
		//given
		DiaryRequest request = new DiaryRequest("a".repeat(201));

		//when
		Set<ConstraintViolation<DiaryRequest>> violations = validator.validate(request);
		ConstraintViolation<DiaryRequest> violation = violations.iterator().next();

		//then
		assertThat(violation).isNotNull();
		assertThat(violation.getMessage()).isEqualTo("회고록은 200글자 이내로 작성해주세요.");

	}
}

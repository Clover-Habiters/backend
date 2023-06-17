package com.clover.habbittracker.domain.habit.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.clover.habbittracker.domain.member.dto.MemberRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class HabitRequestTest {
	private static Validator validator;

	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("닉네임이 12자 이상이면 validation 에러가 발생하며, 에러 메세지를 출력한다.")
	void nickNameMaxSizeTest() {
		//given
		MemberRequest request = new MemberRequest("a".repeat(13));

		//when
		Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);
		ConstraintViolation<MemberRequest> violation = violations.iterator().next();

		//then
		assertThat(violation).isNotNull();
		assertThat(violation.getMessage()).isEqualTo("닉네임은 12자 이내로 입력해주세요.");

	}
}

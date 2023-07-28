package com.clover.habbittracker.domain.bookmark.entity;

import static com.clover.habbittracker.util.MemberProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BookmarkTest {

	private static final String TITLE = "테스트 제목";
	private static final String DESCRIPTION = "테스트 설명";
	private static final String SIZE_500_STRING_DUMMY = "0" + "1234567890".repeat(50);

	private static Stream<Arguments> provideStringDummy() {
		return Stream.of(
			Arguments.of("", true),
			Arguments.of(" ", true),
			Arguments.of(SIZE_500_STRING_DUMMY, true)
		);
	}

	@Test
	@DisplayName("[성공] 북마크 생성")
	void createTest() {
		//given & when
		Bookmark bookmark = Bookmark.builder()
			.title(TITLE)
			.description(DESCRIPTION)
			.member(createTestMember())
			.build();
		//then
		assertThat(bookmark)
			.hasFieldOrPropertyWithValue("title", bookmark.getTitle())
			.hasFieldOrPropertyWithValue("description", bookmark.getDescription());
	}

	@ParameterizedTest
	@DisplayName("[실패] title 유효성 검증 - 제목은 1자 이상 30자 이하만 가능")
	@NullAndEmptySource
	@ValueSource(strings = {" ", "", "01234567890012345678900123456789031"})
	void titleValidateTest(String inputTitle) {
		//given & when & then

		assertThatThrownBy(
			() -> Bookmark.builder()
				.title(inputTitle)
				.description(DESCRIPTION)
				.member(createTestMember())
				.build())
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@DisplayName("[실패] description 유효성 검증 - 설명은 1자 이상 500자 이하만 가능")
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	void descriptionValidateTest(String inputDescription) {
		//given & when & then

		assertThatThrownBy(
			() -> Bookmark.builder()
				.title(TITLE)
				.description(inputDescription)
				.member(createTestMember())
				.build())
			.isInstanceOf(IllegalArgumentException.class);
	}

}

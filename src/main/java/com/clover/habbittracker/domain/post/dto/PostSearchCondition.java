package com.clover.habbittracker.domain.post.dto;

import java.util.Arrays;
import java.util.Objects;

import com.clover.habbittracker.domain.post.entity.Post;
import com.clover.habbittracker.global.base.entity.RestDocsEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCondition {

	private Post.Category category;
	private SearchType searchType;
	private String keyword;

	public enum SearchType implements RestDocsEnum {
		ALL("전체 검색"),
		TITLE("제목 검색"),
		CONTENT("본문 검색");

		private final String description;

		SearchType(String description) {
			this.description = description;
		}

		@JsonCreator
		public static SearchType of(String searchTypeName) {
			return Arrays.stream(SearchType.values())
				.filter(value -> Objects.equals(value.name(), searchTypeName.toUpperCase()))
				.findFirst()
				.orElseThrow();
		}

		@Override
		@JsonValue
		public String getName() {
			return this.name();
		}

		@Override
		public String getDescription() {
			return description;
		}
	}
}
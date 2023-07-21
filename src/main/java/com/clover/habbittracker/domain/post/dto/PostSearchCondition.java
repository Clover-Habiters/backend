package com.clover.habbittracker.domain.post.dto;

import java.util.Arrays;
import java.util.Objects;

import com.clover.habbittracker.domain.post.entity.Post;
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
	public enum SearchType {
		ALL, TITLE, CONTENT;

		@JsonCreator
		public static SearchType of(String searchTypeName) {
			return Arrays.stream(SearchType.values())
				.filter(value -> Objects.equals(value.name(), searchTypeName.toUpperCase()))
				.findFirst()
				.orElseThrow();
		}

		@JsonValue
		public String getName() {
			return this.name();
		}
	}
}
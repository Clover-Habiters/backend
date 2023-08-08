package com.clover.habbittracker.global.restdocs.enums;

import java.util.HashMap;
import java.util.Map;

public class EnumCode {
	private Map<String, String> emojiDomain;
	private Map<String, String> emojiType;
	private Map<String, String> category;
	private Map<String, String> searchType;

	private EnumCode() {
		this.emojiDomain = new HashMap<>();
		this.emojiType = new HashMap<>();
		this.category = new HashMap<>();
		this.searchType = new HashMap<>();
	}

	private EnumCode(Map<String, String> emojiDomain, Map<String, String> emojiType, Map<String, String> category,
		Map<String, String> searchType) {
		this.emojiDomain = emojiDomain;
		this.emojiType = emojiType;
		this.category = category;
		this.searchType = searchType;
	}

	public Map<String, String> getEmojiDomain() {
		return emojiDomain;
	}

	public Map<String, String> getEmojiType() {
		return emojiType;
	}

	public Map<String, String> getCategory() {
		return category;
	}

	public Map<String, String> getSearchType() {
		return searchType;
	}

	public static class EnumDocsBuilder {
		private Map<String, String> emojiDomain;
		private Map<String, String> category;
		private Map<String, String> emojiType;
		private Map<String, String> searchType;

		public EnumDocsBuilder() {
			this.emojiDomain = new HashMap<>();
			this.category = new HashMap<>();
			this.emojiType = new HashMap<>();
			this.searchType = new HashMap<>();
		}

		public EnumDocsBuilder emojiDomain(Map<String, String> emojiDomain) {
			this.emojiDomain.putAll(emojiDomain);
			return this;
		}

		public EnumDocsBuilder emojiType(Map<String, String> emojiType) {
			this.emojiType.putAll(emojiType);
			return this;
		}

		public EnumDocsBuilder category(Map<String, String> category) {
			this.category.putAll(category);
			return this;
		}

		public EnumDocsBuilder searchType(Map<String, String> searchType) {
			this.searchType.putAll(searchType);
			return this;
		}

		public EnumCode build() {
			EnumCode enumCode = new EnumCode();
			enumCode.emojiDomain = this.emojiDomain;
			enumCode.emojiType = this.emojiType;
			enumCode.category = this.category;
			enumCode.searchType = this.searchType;
			return enumCode;
		}
	}

	public static EnumDocsBuilder builder() {
		return new EnumDocsBuilder();
	}
}

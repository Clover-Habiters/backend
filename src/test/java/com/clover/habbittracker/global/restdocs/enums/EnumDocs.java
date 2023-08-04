package com.clover.habbittracker.global.restdocs.enums;

import java.util.HashMap;
import java.util.Map;

public class EnumDocs {
	private Map<String, String> emojiDomain;
	private Map<String, String> emojiType;
	private Map<String, String> category;

	private EnumDocs() {
		this.emojiDomain = new HashMap<>();
		this.emojiType = new HashMap<>();
		this.category = new HashMap<>();
	}

	private EnumDocs(Map<String, String> emojiDomain, Map<String, String> emojiType, Map<String, String> category) {
		this.emojiDomain = emojiDomain;
		this.emojiType = emojiType;
		this.category = category;
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

	public static class EnumDocsBuilder {
		private Map<String, String> emojiDomain;
		private Map<String, String> category;
		private Map<String, String> emojiType;

		public EnumDocsBuilder() {
			this.emojiDomain = new HashMap<>();
			this.category = new HashMap<>();
			this.emojiType = new HashMap<>();
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

		public EnumDocs build() {
			EnumDocs enumDocs = new EnumDocs();
			enumDocs.emojiDomain = this.emojiDomain;
			enumDocs.emojiType = this.emojiType;
			enumDocs.category = this.category;
			return enumDocs;
		}
	}

	public static EnumDocsBuilder builder() {
		return new EnumDocsBuilder();
	}
}

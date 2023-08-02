package com.clover.habbittracker.global.restdocs.util;

public interface DocumentLinkGenerator {
	static String generateLinkCode(DocUrl docUrl) {
		return String.format("link:../enums/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
	}

	enum DocUrl {
		EMOJI_DOMAIN("emoji-domain", "도메인 종류"),
		EMOJI_TYPE("emoji-type", "이모지 타입"),
		CATEGORY("category", "카테고리 종류");

		private final String pageId;
		private final String text;

		DocUrl(String pageId, String text) {
			this.pageId = pageId;
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
}

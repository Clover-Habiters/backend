package com.clover.habbittracker.global.restdocs.util;

public interface DocumentLinkGenerator {
	static String generateLinkCode(DocUrl docUrl) {
		return String.format("link:../%s/%s.html[%s,role=\"popup\"]", docUrl.type, docUrl.pageId, docUrl.text);
	}

	enum DocUrl {
		PAGEABLE("pageable", "common", "페이지 네이션"),
		EMOJI_DOMAIN("emoji-domain", "enums", "도메인 종류"),
		EMOJI_TYPE("emoji-type", "enums", "이모지 타입"),
		CATEGORY("category", "enums", "카테고리 종류"),
		SEARCHTYPE("search-type", "enums", "검색 타입");

		private final String pageId;
		private final String text;

		private final String type;

		DocUrl(String pageId, String type, String text) {
			this.pageId = pageId;
			this.type = type;
			this.text = text;
		}
	}
}

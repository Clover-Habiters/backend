package com.clover.habbittracker.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberReportResponse(
	int numOfBookmark,
	int numOfPost,
	int numOfComment
) {
	@QueryProjection
	public MemberReportResponse {
	}
}

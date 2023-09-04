package com.clover.habbittracker.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberReportResponse(
	int numOfPost,
	int numOfComment,
	int numOfBookmark
) {
	@QueryProjection
	public MemberReportResponse {
	}
}

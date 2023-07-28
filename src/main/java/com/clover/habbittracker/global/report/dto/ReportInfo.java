package com.clover.habbittracker.global.report.dto;

import jakarta.servlet.http.HttpServletRequest;

public record ReportInfo(
	String requestURL,
	String method,
	String remoteAddress
) {
	public ReportInfo(HttpServletRequest request) {
		this(String.valueOf(request.getRequestURL()),
			request.getMethod(),
			request.getRemoteAddr()
		);
	}
}

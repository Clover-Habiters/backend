package com.clover.habbittracker.global.infra.alarm.dto;

import jakarta.servlet.http.HttpServletRequest;

public record AlarmInfo(
	String requestURL,
	String method,
	String remoteAddress
) {
	public AlarmInfo(HttpServletRequest request) {
		this(String.valueOf(request.getRequestURL()),
			request.getMethod(),
			request.getRemoteAddr()
		);
	}
}

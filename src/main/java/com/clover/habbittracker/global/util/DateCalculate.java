package com.clover.habbittracker.global.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DateCalculate {
	public static final String FORMAT = "-01T00:00:00.000000";

	public static Map<String, LocalDateTime> startEnd(String date) {
		Map<String, LocalDateTime> result = new HashMap<>();
		if (date == null) {
			date = LocalDateTime.now().toString().substring(0, 7);
		}
		LocalDateTime parse = LocalDateTime.parse(date + FORMAT);
		result.put("start", parse.withDayOfMonth(1));
		result.put("end", parse.plusMonths(1));
		return result;
	}

}

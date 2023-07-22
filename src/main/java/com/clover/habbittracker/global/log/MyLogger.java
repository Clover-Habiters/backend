package com.clover.habbittracker.global.log;

import static org.springframework.web.servlet.HandlerMapping.*;

import org.springframework.web.method.HandlerMethod;

import com.clover.habbittracker.global.exception.BaseException;
import com.clover.habbittracker.global.exception.ErrorType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyLogger {

	private static final String LOG_FORM = "[{}]: Called By method [{}] Cause : {} id = {}";

	private static final String COMMON_LOG_FORM = "[{}]: Called By method {} Cause : {}";

	public static void warnExceptionLogging(HttpServletRequest request, BaseException e) {
		ErrorType errorType = e.getErrorType();
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();

		log.warn(LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg(), e.getReason());
	}

	public static void commonExceptionWarnLogging(HttpServletRequest request, Exception e, ErrorType errorType) {
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();

		log.warn(COMMON_LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg());
	}

	public static void commonExceptionErrorLogging(HttpServletRequest request, Exception e, ErrorType errorType) {
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();

		log.warn(COMMON_LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg());
	}

}

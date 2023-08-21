package com.clover.habbittracker.global.infra.log;

import static com.clover.habbittracker.global.infra.log.MyLogger.ClientIPHeader.*;
import static org.springframework.web.servlet.HandlerMapping.*;

import org.springframework.web.method.HandlerMethod;

import com.clover.habbittracker.global.base.exception.BaseException;
import com.clover.habbittracker.global.base.exception.ErrorType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyLogger {

	private static final String LOG_FORM = "[{}]: Called By method [{}] Cause : {} id = {}";

	private static final String COMMON_LOG_FORM = "[{}]: Called By method {} Cause : {} Reason : {} client IP : {}";

	public static void warnExceptionLogging(HttpServletRequest request, BaseException e) {
		ErrorType errorType = e.getErrorType();
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();

		log.warn(LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg(), e.getReason());
	}

	public static void commonExceptionWarnLogging(HttpServletRequest request, Exception e, ErrorType errorType) {
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();
		String clientIP = getClientIP(request);
		log.warn(COMMON_LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg(), e.getMessage(),
			clientIP);
	}

	public static void commonExceptionErrorLogging(HttpServletRequest request, Exception e, ErrorType errorType) {
		String methodName
			= ((HandlerMethod)request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE)).getMethod().getName();
		String clientIP = getClientIP(request);
		log.warn(COMMON_LOG_FORM, e.getClass().getSimpleName(), methodName, errorType.getErrorMsg(), e.getMessage(),
			clientIP);
	}

	public enum ClientIPHeader {
		X_FORWARDED_FOR("X-Forwarded-For"),
		PROXY_CLIENT_IP("Proxy-Client-IP"),
		WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
		HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
		HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
		REMOTE_ADDR(null);

		private final String headerName;

		ClientIPHeader(String headerName) {
			this.headerName = headerName;
		}

		public static String getClientIP(HttpServletRequest request) {
			String ip = null;
			for (ClientIPHeader header : values()) {
				ip = request.getHeader(header.headerName);
			}
			if (ip == null) {
				ip = request.getRemoteAddr();
			}
			return ip;
		}
	}
}

package com.clover.habbittracker.global.infra.alaram;

import static org.mockito.BDDMockito.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.clover.habbittracker.global.infra.alarm.SlackAlarmSender;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class SlackAlarmSenderTest {

	private final String TEST_WEBHOOK = "https://hooks.slack.com/services/abcd";

	private final Exception exception = new RuntimeException("test exception message");

	@Mock
	private HttpServletRequest request;

	@Mock
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	private SlackAlarmSender slackAlarmSender;

	@BeforeEach
	void setUp() {
		slackAlarmSender = new SlackAlarmSender(TEST_WEBHOOK, threadPoolTaskExecutor);
	}

	@Test
	@DisplayName("[성공] 예외가 발생하면 슬랙으로 알림이 전송된다.")
	void slackNotificateTest() throws Throwable { // 비동기 실행되는 메서드(sendSlackMessage)는 어떻게 테스트해야하는가?
		// Given
		given(request.getRequestURL()).willReturn(new StringBuffer("http://localhost:8080"));
		given(request.getMethod()).willReturn("GET");
		given(request.getRemoteAddr()).willReturn("192.0.0.2");

		// When
		slackAlarmSender.sendReport(proceedingJoinPoint, request, exception);

		// Then
		then(proceedingJoinPoint).should().proceed(); // AOP 동작확인
		then(threadPoolTaskExecutor).should().execute(any(Runnable.class)); // 비동기 실행확인
	}

}




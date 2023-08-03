package com.clover.habbittracker.global.infra.alarm;

import static java.util.Collections.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import com.clover.habbittracker.global.infra.alarm.dto.AlarmInfo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Profile({"prod"})
public class SlackAlarmSender {

	private final SlackApi slackApi;
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public SlackAlarmSender(
		@Value("${slack.webhook}") String webhook,
		ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.slackApi = new SlackApi(webhook);
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	@Around("@annotation(com.clover.habbittracker.global.infra.alarm.annotation.Alarm) && args(request, e)")
	public void sendReport(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request,
		Exception e) throws Throwable {
		proceedingJoinPoint.proceed();
		AlarmInfo requestInfo = new AlarmInfo(request);
		threadPoolTaskExecutor.execute(() -> sendSlackMessage(requestInfo, e)); // 비동기
	}

	private void sendSlackMessage(AlarmInfo request, Exception e) {
		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("danger");

		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("Exception class").setValue(e.getClass().getCanonicalName()),
				new SlackField().setTitle("예외 메시지").setValue(e.getMessage()),
				new SlackField().setTitle("Request URI").setValue(request.requestURL()),
				new SlackField().setTitle("Request Method").setValue(request.method()),
				new SlackField().setTitle("요청 시간")
					.setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),
				new SlackField().setTitle("Request IP").setValue(request.remoteAddress())
			)
		);

		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setAttachments(singletonList(slackAttachment));
		slackMessage.setText("에러 발생!");
		slackMessage.setUsername("HabitersBot");

		slackApi.call(slackMessage);
	}
}

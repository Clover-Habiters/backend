package com.clover.habbittracker.global.infra.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Profile("default")
public class LoggingAspect {

	@Pointcut("execution(* com.clover.habbittracker.domain..*(..))")
	public void methodInDomain() {
	}

	// 메서드 실행시간 분석
	@Around("methodInDomain()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long endTime = System.currentTimeMillis();

		long executionTimeMillis = endTime - startTime;

		Signature signature = joinPoint.getSignature();

		String methodName = signature.getName();
		String className = signature.getDeclaringType().getSimpleName();
		log.info("[ExecutionTime] : {}.{} took {}ms", className, methodName, executionTimeMillis);

		return result;
	}
}

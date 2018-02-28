package kr.co.smh.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAdvice {

	Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);

	//user 관련된 서비스가 실행 될 때의 포인트컷
	@Around("within(kr.co.smh.service.user..*)")
	// @Around("execution(* kr.co.smh.service.user..*(..))")
	public Object userLogger(ProceedingJoinPoint joinPoint) throws Throwable {
		String signature = joinPoint.getSignature().toShortString();
		logger.info("=============="+signature + " 실행 시작합니다.=============");
		try {
			Object obj = joinPoint.proceed();
			return obj;
		} finally {
			logger.info("============="+signature + " 실행 종료합니다.=============");
		}
	}
}
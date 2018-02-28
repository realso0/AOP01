package kr.co.smh.aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.smh.domain.mongo.dto.LogDTO;
import kr.co.smh.mongo.log.MongoLogger;

@Component
@Aspect
public class TimeCheckAdvice {

	@Autowired
	private HttpSession session;

	@Autowired
	private MongoLogger mongoLogger;

	private static final Logger logger = LoggerFactory.getLogger(TimeCheckAdvice.class);

	// @Around("execution(* kr.co.smh.service..*(..))")
	@Around("within(kr.co.smh.service..*)")
	public Object timeLog(ProceedingJoinPoint joinPoint) throws Throwable {
		String signature = joinPoint.getSignature().toShortString();
		logger.info(signature + " is start");
		long st = System.currentTimeMillis();

		try {
			logger.info(signature + " Running");
			Object obj = joinPoint.proceed();
			return obj;
		} finally {
			long et = System.currentTimeMillis();
			LogDTO logDTO = new LogDTO();
			logDTO.setSignature(signature);
			logDTO.setCurrentTime(et - st);
			mongoLogger.insertLog(logDTO);
			logger.info(signature + " is finished");
			logger.info(signature + " 경과시간 : " + (et - st));
		}
	}
}
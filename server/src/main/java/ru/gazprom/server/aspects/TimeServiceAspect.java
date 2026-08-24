package ru.gazprom.server.aspects;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class TimeServiceAspect {


    private static final Logger log = LogManager.getLogger(TimeServiceAspect.class);

    private final MeterRegistry meterRegistry;

    public TimeServiceAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Pointcut("execution(* ru.gazprom.server.service.*.*(..))")
    public void logTime() {}

    @Around("logTime()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature methodSignature = joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();


        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            log.info("{} {} SUCCESS FOR = {} MS", className, methodName, stopWatch.getTotalTimeMillis());
            return result;
        } catch(Throwable exception) {
            stopWatch.stop();
            log.info("{} {} FAILED AFTER = {} MS", className, methodName, stopWatch.getTotalTimeMillis());
            throw exception;
        } finally {
            Timer.builder("service.time.execution")
                    .tag("className", className)
                    .tag("methodName", methodName)
                    .description("Time executing service")
                    .publishPercentileHistogram()
                    .register(meterRegistry)
                    .record(stopWatch.getTotalTimeMillis(), TimeUnit.MILLISECONDS);

            /*1
            new Timer.Builder("service.time.execution")
                    .tag("className", className);

            2
            Timer.Builder timerBuilder = new Timer.Builder("service.time.execution");
            timerBuilder.tag("className", className);*/


        }
    }


}

package com.example.RateLimiter.aspects;

import com.example.RateLimiter.annotations.RateLimit;
import com.example.RateLimiter.configs.RateLimiterConfig;
import com.example.RateLimiter.exceptions.RateLimitExceededException;
import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class RateLimitingAspect {

    private final RateLimiterConfig rateLimiterConfig;

    @Pointcut("@annotation(rateLimit)")
    public void rateLimitPointcut(RateLimit rateLimit) {}

    @Around("rateLimitPointcut(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Bucket bucket = rateLimiterConfig.getBucket(methodName);

        if (bucket == null) {
            rateLimiterConfig.registerRateLimit(methodName, rateLimit.requestsPerMinute());
            bucket = rateLimiterConfig.getBucket(methodName);
        }

        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            throw new RateLimitExceededException("Rate limit exceeded for method: " + methodName);
        }
    }
}

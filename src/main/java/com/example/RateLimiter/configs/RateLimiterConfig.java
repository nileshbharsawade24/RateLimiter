package com.example.RateLimiter.configs;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Bandwidth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class RateLimiterConfig {

    private final ConcurrentMap<String, Bucket> bucketMap = new ConcurrentHashMap<>();


    public void registerRateLimit(String methodName, int requestsPerMinute) {
        Bandwidth limit = Bandwidth.simple(requestsPerMinute, Duration.ofSeconds(60));  // Refills more frequently
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();
        bucketMap.put(methodName, bucket);
    }

    public Bucket getBucket(String methodName) {
        return bucketMap.get(methodName);
    }
}

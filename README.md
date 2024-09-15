# Rate Limiter Project
This project implements a rate limiter in a Spring Boot application using Bucket4j. It ensures that API requests are limited based on predefined rules to prevent overuse and abuse.

## Components
- RateLimit Annotation: Defines the maximum number of requests allowed per minute for annotated methods.
- RateLimitingAspect: Uses Aspect-Oriented Programming (AOP) to enforce rate limits on methods annotated with @RateLimit. It manages request counts and throws an exception when limits are exceeded.
- RateLimiterConfig: Configures and maintains rate limit buckets for different methods, handling rate limit registration and retrieval.
- RateLimitExceededException: Custom exception thrown when rate limits are exceeded.
- GlobalExceptionHandler: Handles RateLimitExceededException and responds with a 429 Too Many Requests status.

## Usage
- Annotate controller methods with @RateLimit(requestsPerMinute = <number>) to apply rate limiting.
- The rate limiter will enforce limits based on the configured rate per minute.

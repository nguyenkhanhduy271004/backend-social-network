package com.project.social_network.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  private Bucket resolveBucket(String clientId) {
    return buckets.computeIfAbsent(clientId, k -> newBucket());
  }

  private Bucket newBucket() {
    Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
    Bandwidth limit = Bandwidth.classic(10, refill);
    return Bucket.builder().addLimit(limit).build();
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String clientId = request.getRemoteAddr();
    Bucket bucket = resolveBucket(clientId);

    if (bucket.tryConsume(1)) {
      return true; // Cho phép request
    } else {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.getWriter().write("Too many requests");
      return false;
    }
  }
}
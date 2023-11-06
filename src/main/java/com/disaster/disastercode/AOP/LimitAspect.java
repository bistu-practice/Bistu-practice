package com.disaster.disastercode.AOP;


import com.disaster.disastercode.Annotation.RateLimiter;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
@Order(2)
public class LimitAspect {

    private static final ConcurrentMap<String, com.google.common.util.concurrent.RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Resource
    private HttpServletRequest request;
    @Pointcut("@annotation(com.disaster.disastercode.Annotation.RateLimiter)")
    public void rateLimit() {

    }



    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method,RateLimiter.class);
        System.out.println(method.getName()+IPUtils.getIpAddress(request));
        if (rateLimiter != null && rateLimiter.qps() > RateLimiter.NOT_LIMITED) {
            double qps = rateLimiter.qps();
            if (RATE_LIMITER_CACHE.get(method.getName()+IPUtils.getIpAddress(request)) == null) {
                // 初始化 QPS
                RATE_LIMITER_CACHE
                        .put(method.getName()+IPUtils.getIpAddress(request), com.google.common.util.concurrent
                                .RateLimiter.create(qps));
            }
            // 尝试获取令牌
            if (RATE_LIMITER_CACHE
                    .get(method.getName()+IPUtils.getIpAddress(request)) != null
                    && !RATE_LIMITER_CACHE.get(method.getName()+IPUtils.getIpAddress(request))
                    .tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                throw new BusinessException(ErrorCode.OUT_OF_SPEND);
            }
        }
        return point.proceed();
    }
    @Scheduled(cron = "0 0 * * * *") // 每小时执行一次
    private void cleanCache(){
        RATE_LIMITER_CACHE.clear();
        System.out.println("缓存已清空");
    }

}

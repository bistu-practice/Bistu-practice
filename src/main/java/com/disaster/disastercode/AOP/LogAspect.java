package com.disaster.disastercode.AOP;

import com.disaster.disastercode.Annotation.LogAnnotation;
import com.disaster.disastercode.DTO.SafeUserDTO;
import com.disaster.disastercode.model.domain.OperLog;
import com.disaster.disastercode.service.OperlogService;
import com.disaster.disastercode.service.UserService;
import com.disaster.disastercode.utils.IPUtils;
import com.disaster.disastercode.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@Order(1)
public class LogAspect implements BeanFactoryAware {
    @Resource
    private BeanFactory beanFactory;
    @Resource
    private UserService userService;

    @Resource
    private OperlogService operlogService;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, LogAnnotation controllerLog, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // === handleLog是经过代理的异步方法，不能直接简单的进行方法调用
        // === 正确的调用方法是，获取bean对象（这是个代理对象）调用
        LogAspect logAspect = beanFactory.getBean(LogAspect.class);
        logAspect.handleLog(joinPoint, controllerLog, null, result, request);
    }
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, LogAnnotation controllerLog, Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        LogAspect logAspect = beanFactory.getBean(LogAspect.class);
        logAspect.handleLog(joinPoint, controllerLog, e, null, request);
    }
    @Async("Executor")
    public void handleLog(final JoinPoint joinPoint, LogAnnotation controllerLog, final Exception e, Object result, HttpServletRequest request) {
        try {
            // 获取当前的用户
            SafeUserDTO loginUser = userService.getCurrentUser(request);
            if (loginUser!=null){
                OperLog operLog = new OperLog();
                String className = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                operLog.setOperationIp(IPUtils.getIpAddress(request));
                operLog.setRequestMethod(className + "." + methodName + "()");
                operLog.setMethod(request.getMethod());
                operLog.setOperationPersonId(loginUser.getId());
                operLog.setOperationPersonName(loginUser.getUserName());
                operLog.setContent(controllerLog.content());
                operLog.setStatus(200);
                operLog.setOperationTime(new Date());
                operLog.setOperationUrl(request.getRequestURI());
                operLog.setBusinessType(controllerLog.businessType());
                operLog.setErrorMsg("无异常");
                if (e != null) {
                    operLog.setStatus(500);
                    operLog.setErrorMsg(e.getMessage().substring(0, Math.min(2000, e.getMessage().length())));
                }
                operlogService.insert(operLog);
            }

        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }
}

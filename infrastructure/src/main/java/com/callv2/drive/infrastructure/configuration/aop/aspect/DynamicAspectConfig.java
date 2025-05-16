package com.callv2.drive.infrastructure.configuration.aop.aspect;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;

import com.callv2.drive.infrastructure.aop.aspects.chain.MethodInvocationAspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.PostInvocationAspectExecutorChain;

public class DynamicAspectConfig {

    @Bean
    public List<Advisor> dynamicAdvisors(
            MethodInvocationAspectExecutorChain beforeChain,
            PostInvocationAspectExecutorChain afterChain,
            PostInvocationAspectExecutorChain errorChain) {

        return List.of();
        // return properties.getDynamicLoggers().stream()
        // .map(cfg -> buildAdvisor("cfg.getExpression()", beforeChain, afterChain,
        // errorChain))
        // .toList();
    }

    private static Advisor buildAdvisor(final String expression, final MethodInterceptor interceptor) {
        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        // MethodInterceptor interceptor = invocation -> {

        // return invocation.proceed();

        // return invocation.proceed();
        // // new MethodInvocationProceedingJoinPoint(invocation);
        // // JoinPoint joinPoint = new MethodInvocationJoinPoint(invocation);
        // // AbstractAroundAspectHandler handler = new
        // // AbstractAroundAspectHandler(beforeChain, afterChain, errorChain);
        // // return handler.defaultHandle(joinPoint);
        // };

        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }
}

package com.callv2.drive.infrastructure.configuration.aop.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.logging.log4j.Level;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.infrastructure.aop.aspects.chain.AspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.MethodInvocationAspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.PostInvocationAspectExecutorChain;
import com.callv2.drive.infrastructure.aop.aspects.handler.SimpleMethodInterceptorWithContextHandler;
import com.callv2.drive.infrastructure.logging.aspect.executor.MethodSignatureLogExecutor;
import com.callv2.drive.infrastructure.logging.aspect.executor.PostTelemetryLogExecutor;
import com.callv2.drive.infrastructure.logging.aspect.executor.ThrowableLogExecutor;

@Configuration
public class AspectConfig {

    @Bean
    Advisor applicationAdvisor() {

        final MethodInvocationAspectExecutorChain beforeChain = AspectExecutorChain.Builder
                .create(MethodInvocationAspectExecutorChain.class)
                .add(MethodInvocationAspectExecutorChain
                        .with(new MethodSignatureLogExecutor(Level.INFO, MethodSignatureLogExecutor.class)))
                .build();

        final PostInvocationAspectExecutorChain afterChain = AspectExecutorChain.Builder
                .create(PostInvocationAspectExecutorChain.class)
                .add(PostInvocationAspectExecutorChain
                        .with(new PostTelemetryLogExecutor(Level.INFO, PostTelemetryLogExecutor.class)))
                .build();

        final PostInvocationAspectExecutorChain errorChain = AspectExecutorChain.Builder
                .create(PostInvocationAspectExecutorChain.class)
                .add(PostInvocationAspectExecutorChain
                        .with(ThrowableLogExecutor.defaultCreate(ThrowableLogExecutor.class)))
                .build();

        final var ahamm = new SimpleMethodInterceptorWithContextHandler(beforeChain, afterChain, errorChain);

        return applicationAdvisor("execution(* com.callv2.drive.application..*.*(..))", ahamm);
    }

    private static Advisor applicationAdvisor(
            final String expression,
            final MethodInterceptor interceptor) {

        // final String expression = "execution(* com.callv2.drive..*.*(..))";

        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        return new DefaultPointcutAdvisor(
                pointcut,
                interceptor);
    }

}

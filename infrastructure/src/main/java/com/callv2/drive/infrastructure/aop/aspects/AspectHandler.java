package com.callv2.drive.infrastructure.aop.aspects;

import org.aspectj.lang.JoinPoint;

public interface AspectHandler {

    Object handle(JoinPoint joinPoint) throws Throwable;

}
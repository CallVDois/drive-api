package com.callv2.drive.infrastructure.aop.aspects.executor;

import org.aopalliance.intercept.Joinpoint;

@FunctionalInterface
public interface AspectExecutor<J extends Joinpoint> {

    void execute(J joinPoint);

}

package com.callv2.drive.infrastructure.aop.aspects.executor;

import org.aopalliance.intercept.Joinpoint;

public interface IdentifiableAspectExecutor<J extends Joinpoint> extends AspectExecutor<J> {

    default String getId() {
        return getClass().getSimpleName();
    }

}

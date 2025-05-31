package com.callv2.drive.infrastructure.aop.aspects.chain;

import org.aopalliance.intercept.Joinpoint;

@FunctionalInterface
public interface Proceeder<J extends Joinpoint, O> {

    O proceed(J joinpoint) throws Throwable;

}
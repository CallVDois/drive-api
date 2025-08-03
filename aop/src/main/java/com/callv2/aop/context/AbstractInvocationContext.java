package com.callv2.aop.context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

public abstract class AbstractInvocationContext implements ProceedingJoinPoint {

    protected final ProceedingJoinPoint joinPoint;

    protected AbstractInvocationContext(final ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    @Override
    public void set$AroundClosure(AroundClosure arc) {
        this.joinPoint.set$AroundClosure(arc);
    }

    @Override
    public String toShortString() {
        return this.joinPoint.toShortString();
    }

    @Override
    public String toLongString() {
        return this.joinPoint.toLongString();
    }

    @Override
    public Object getThis() {
        return this.joinPoint.getThis();
    }

    @Override
    public Object getTarget() {
        return this.joinPoint.getTarget();
    }

    @Override
    public Object[] getArgs() {
        return this.joinPoint.getArgs();
    }

    @Override
    public Signature getSignature() {
        return this.joinPoint.getSignature();
    }

    @Override
    public SourceLocation getSourceLocation() {
        return this.joinPoint.getSourceLocation();
    }

    @Override
    public String getKind() {
        return this.joinPoint.getKind();
    }

    @Override
    public StaticPart getStaticPart() {
        return this.joinPoint.getStaticPart();
    }

}

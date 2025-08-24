package com.callv2.aop.context;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

public abstract class AbstractInvocationContext implements InvocationContext {

    protected final ProceedingJoinPoint joinPoint;
    protected final AtomicBoolean proceeded;
    private final Instant contextedAt;

    protected AbstractInvocationContext(final ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        this.proceeded = new AtomicBoolean(false);
        this.contextedAt = Instant.now();
    }

    protected AbstractInvocationContext(final PreInvocationContext preInvocationContext) {
        this.joinPoint = preInvocationContext;
        this.proceeded = new AtomicBoolean(preInvocationContext.proceeded());
        this.contextedAt = preInvocationContext.getContextedAt();
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

    @Override
    public Instant getContextedAt() {
        return this.contextedAt;
    }

    @Override
    public boolean proceeded() {
        return this.proceeded.get();
    }

}

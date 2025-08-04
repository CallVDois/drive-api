package com.callv2.aop.context;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SimplePostInvocationContext extends AbstractInvocationContext implements PostInvocationContext {

    private final Object result;
    private final Throwable throwable;
    private final Instant proceededAt;
    private final AtomicBoolean successful;

    private SimplePostInvocationContext(
            final PreInvocationContext preInvocationContext,
            final Object result,
            final Throwable throwable,
            final Instant proceededAt,
            final boolean successful) {
        super(preInvocationContext);
        this.result = result;
        this.throwable = throwable;
        this.proceededAt = proceededAt;
        this.successful = new AtomicBoolean(successful);
    }

    protected static SimplePostInvocationContext from(final PreInvocationContext preInvocationContext) {

        Object result;
        Throwable throwable;
        boolean successful;
        final Instant proceededAt;

        try {
            result = preInvocationContext.proceed();
            throwable = null;
            successful = true;
        } catch (Throwable e) {
            result = null;
            throwable = e;
            successful = false;
        } finally {
            proceededAt = Instant.now();
        }

        return new SimplePostInvocationContext(
                preInvocationContext,
                result,
                throwable,
                proceededAt,
                successful);

    }

    @Override
    public Object proceed() throws Throwable {
        return this.joinPoint.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        return this.joinPoint.proceed(args);
    }

    @Override
    public Instant getProceededAt() {
        return this.proceededAt;
    }

    @Override
    public Object getResult() {
        return this.result;
    }

    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override
    public boolean wasSuccessful() {
        return this.successful.get();
    }

}

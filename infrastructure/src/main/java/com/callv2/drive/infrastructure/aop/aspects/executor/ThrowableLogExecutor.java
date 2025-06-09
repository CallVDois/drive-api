package com.callv2.drive.infrastructure.aop.aspects.executor;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import org.apache.logging.log4j.Level;

public class ThrowableLogExecutor extends Log4jExecutor<PostInvocationContext> {

    private ThrowableLogExecutor(final Level level, final Class<?> clazz) {
        super(level, clazz);
    }

    public static ThrowableLogExecutor defaultCreate(final Class<?> clazz) {
        return new ThrowableLogExecutor(Level.ERROR, clazz);
    }

    public static ThrowableLogExecutor create(final Level level, final Class<?> clazz) {
        return new ThrowableLogExecutor(level, clazz);
    }

    @Override
    public void execute(final PostInvocationContext context) {
        if (context.getThrowable() != null)
            log("<<EXCEPTION>> [{}] <<EXCEPTION-MESSAGE>> [{}] <<METHOD-CALLED>> [{}]",
                    context.getThrowable().getClass().getName(),
                    context.getThrowable().getMessage(),
                    context.getMethod().toString(),
                    context.getThrowable());
    }

}

package com.callv2.drive.infrastructure.logging.aspect.executor;

import com.callv2.drive.infrastructure.aop.aspects.context.PostInvocationContext;
import org.apache.logging.log4j.Level;

public class ThrowableLogExecutor extends Log4jPostExecutor {

    private ThrowableLogExecutor(final Level level, final Class<?> clazz) {
        super(level, clazz);
    }

    public static ThrowableLogExecutor defaultExecutor(final Class<?> clazz) {
        return new ThrowableLogExecutor(Level.ERROR, clazz);
    }

    public static ThrowableLogExecutor create(final Level level, final Class<?> clazz) {
        return new ThrowableLogExecutor(level, clazz);
    }

    @Override
    public void execute(final PostInvocationContext context) {
        if (context.getThrowable() != null)
            log(context.getThrowable());
    }

}

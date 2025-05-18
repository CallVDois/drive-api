package com.callv2.drive.infrastructure.aop.aspects.executor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.callv2.drive.infrastructure.aop.aspects.context.MethodInvocationContext;

public class ArgsLogExecutor extends Log4jExecutor<MethodInvocationContext> {

    public ArgsLogExecutor(final Level level, final Class<?> clazz) {
        super(level, clazz);
    }

    @Override
    public void execute(final MethodInvocationContext context) {
        final var args = context.getArguments();
        log("<<METHOD-CALLED>> [{}] <<ARGS>> count:[{}] args: [{}]",
                context.getMethod(),
                args.length,
                Arrays.toString(args));
    }

}

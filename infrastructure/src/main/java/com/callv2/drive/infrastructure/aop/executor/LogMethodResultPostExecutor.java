package com.callv2.drive.infrastructure.aop.executor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.callv2.aop.context.PostInvocationContext;
import com.callv2.aop.executor.PostExecutor;

public class LogMethodResultPostExecutor implements PostExecutor {

    private final Log4jLogger logger;

    public LogMethodResultPostExecutor(final Level logLevel) {
        this.logger = new Log4jLogger(logLevel, LogMethodResultPostExecutor.class);
    }

    public LogMethodResultPostExecutor(final Level logLevel, final Class<?> clazz) {
        this.logger = new Log4jLogger(logLevel, clazz);
    }

    @Override
    public void execute(final PostInvocationContext joinPoint) {

        logger.log("<<METHOD-CALLED>> [{}] <<ARGS>> [{}] <<RESULT>> [{}]",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()),
                joinPoint.getResult());
    }

}

package com.callv2.drive.infrastructure.configuration.aop;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.aop.executor.chain.handler.ExecutorChainHandler;
import com.callv2.aop.executor.chain.handler.SimpleExecutorChainHandlerBuilder;
import com.callv2.drive.infrastructure.aop.executor.LogErrorPostExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogMethodArgsPreExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogMethodSignaturePreExecutor;
import com.callv2.drive.infrastructure.aop.executor.LogTelemetryPostExecutor;

@Configuration
public class AopConfig {

    @Bean
    ExecutorChainHandler executorChainHandler() {

        return SimpleExecutorChainHandlerBuilder
                .create()
                .preExecutor(new LogMethodSignaturePreExecutor(Level.INFO))
                .preExecutor(new LogMethodArgsPreExecutor(Level.DEBUG))
                .postExecutor(new LogTelemetryPostExecutor(Level.INFO))
                .errorExecutor(new LogErrorPostExecutor(Level.ERROR))
                .build();
    }

}

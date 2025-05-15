package com.callv2.drive.infrastructure.configuration.logging;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.infrastructure.aop.aspects.chain.AspectAfterExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectBeforeExecutionChain;
import com.callv2.drive.infrastructure.aop.aspects.chain.AspectExecutionErrorChain;
import com.callv2.drive.infrastructure.aop.aspects.execution.after.TelemetryAfterLogAspect;
import com.callv2.drive.infrastructure.aop.aspects.execution.before.ArgsLogAspect;
import com.callv2.drive.infrastructure.aop.aspects.execution.before.SignatureLogAspect;
import com.callv2.drive.infrastructure.aop.aspects.execution.before.TelemetryBeforeLogAspect;
import com.callv2.drive.infrastructure.aop.aspects.execution.error.StackTraceLogAspect;
import com.callv2.drive.infrastructure.logging.domain.FolderLog;

@Configuration
public class DomainLogConfig {

    FolderLog folderLog() {
        final var beforeChain = AspectBeforeExecutionChain.Builder.create()
                .add(new SignatureLogAspect(Level.INFO))
                .add(new ArgsLogAspect(Level.INFO))
                .add(new TelemetryBeforeLogAspect(Level.INFO))
                .build();

        final var afterChain = AspectAfterExecutionChain.Builder.create()
                .add(new TelemetryAfterLogAspect(Level.INFO))
                .build();

        final var errorChain = AspectExecutionErrorChain.Builder.create()
                .add(new StackTraceLogAspect(Level.INFO))
                .build();

        return new FolderLog(beforeChain, afterChain, errorChain);
    }

}

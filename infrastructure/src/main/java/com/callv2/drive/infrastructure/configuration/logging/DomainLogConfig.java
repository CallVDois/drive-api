package com.callv2.drive.infrastructure.configuration.logging;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainLogConfig {

//     @Bean
//     FolderLog folderLog() {
//         final var beforeChain = AspectBeforeExecutionChain.Builder.create()
//                 .add(new SignatureLogAspect(Level.INFO))
//                 .add(new ArgsLogAspect(Level.INFO))
//                 .add(new TelemetryBeforeLogAspect(Level.DEBUG))
//                 .build();

//         final var afterChain = AspectAfterExecutionChain.Builder.create()
//                 .add(new TelemetryAfterLogAspect(Level.INFO))
//                 .build();

//         final var errorChain = AspectExecutionErrorChain.Builder.create()
//                 .add(new StackTraceLogAspect(Level.INFO))
//                 .build();

//         return new FolderLog(beforeChain, afterChain, errorChain);
//     }

//     @Bean
//     FileLog fileLog() {
//         final var beforeChain = AspectBeforeExecutionChain.Builder.create()
//                 .add(new SignatureLogAspect(Level.INFO))
//                 .add(new ArgsLogAspect(Level.INFO))
//                 // .add(new TelemetryBeforeLogAspect(Level.INFO))
//                 .build();

//         final var afterChain = AspectAfterExecutionChain.Builder.create()
//                 .add(new TelemetryAfterLogAspect(Level.INFO))
//                 .build();

//         final var errorChain = AspectExecutionErrorChain.Builder.create()
//                 .add(new StackTraceLogAspect(Level.INFO))
//                 .build();

//         return new FileLog(beforeChain, afterChain, errorChain);
//     }

//     @Bean
//     StorageLog storageLog() {
//         final var beforeChain = AspectBeforeExecutionChain.Builder.create()
//                 .add(new SignatureLogAspect(Level.INFO))
//                 .add(new ArgsLogAspect(Level.INFO))
//                 // .add(new TelemetryBeforeLogAspect(Level.INFO))
//                 .build();

//         final var afterChain = AspectAfterExecutionChain.Builder.create()
//                 .add(new TelemetryAfterLogAspect(Level.INFO))
//                 .build();

//         final var errorChain = AspectExecutionErrorChain.Builder.create()
//                 .add(new StackTraceLogAspect(Level.INFO))
//                 .build();

//         return new StorageLog(beforeChain, afterChain, errorChain);
//     }

}

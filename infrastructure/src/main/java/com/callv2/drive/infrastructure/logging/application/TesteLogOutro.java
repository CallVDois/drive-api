package com.callv2.drive.infrastructure.logging.application;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// @Aspect
// @Component
public class TesteLogOutro  {//extends AbstractAroundAspectHandler {

//     protected TesteLogOutro() {
//         super(
//                 AspectBeforeExecutionChain.Builder
//                         .create()
//                         .add(new SignatureLogAspect(Level.INFO))
//                         .add(new ArgsLogAspect(Level.INFO))
//                         .add(new TelemetryBeforeLogAspect(Level.INFO))
//                         .build(),
//                 AspectAfterExecutionChain.Builder
//                         .create()
//                         .add(new TelemetryAfterLogAspect(Level.INFO))
//                         .build(),
//                 AspectExecutionErrorChain.Builder
//                         .create()
//                         .add(new StackTraceLogAspect(Level.INFO))
//                         .build());
//     }

//     @Override
//     @Around("execution(* com.callv2.drive.application..*(..))")
//     public Object handle(final JoinPoint joinPoint) throws Throwable {
//         return this.defaultHandle(joinPoint);
//     }

}

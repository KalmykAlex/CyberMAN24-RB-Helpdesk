package com.example.helpdesk;

import com.example.helpdesk.auth.RegisterRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.helpdesk.auth.AuthenticationService.register(..)) && args(request)")
    public void logBeforeRegister(JoinPoint joinPoint, RegisterRequest request) {
        logger.info("Tried the register method for email: {}", request.getEmail());
    }


    @Before("execution(* com.example.helpdesk.auth.AuthenticationService.register(..)) && args(request)")
    public void logBeforeUpload(JoinPoint joinPoint, RegisterRequest request) {
        logger.info("Uploaded the file: {} at \\<Project base>\\static", request.getEmail());
    }

}

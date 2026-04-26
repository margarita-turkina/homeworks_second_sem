package com.example.todolist.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.example.todolist.service.*.*(..))")
  public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();

    System.out.println(" [START] " + methodName);
    if (args.length > 0) {
      System.out.println("   Аргументы: " + Arrays.toString(args));
    }

    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();
      long endTime = System.currentTimeMillis();

      System.out.println(" [END] " + methodName);
      System.out.println("  Время: " + (endTime - startTime) + " ms");
      if (result != null) {
        System.out.println("   Результат: " + result);
      }
      return result;
    } catch (Exception e) {
      System.out.println(" [ERROR] " + methodName + " - " + e.getMessage());
      throw e;
    }
  }
}
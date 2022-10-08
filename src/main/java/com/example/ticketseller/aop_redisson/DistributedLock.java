package com.example.ticketseller.aop_redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key() default "";

    long waitTime() default 100L;

    long leaseTime() default 10L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

}

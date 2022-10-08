package com.example.ticketseller.aop_redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class RLockAspect {

    private final RedissonClient redissonClient;
    private final DistributedLockExpressionEvaluator evaluator = new DistributedLockExpressionEvaluator();

    @Around("@annotation(annotation)")
    public Object aroundRLock(ProceedingJoinPoint joinPoint, DistributedLock annotation) throws Throwable {

        var sig = (MethodSignature) joinPoint.getSignature();
        var lockKey = evaluator.evaluate(annotation.key(), sig.getMethod(), joinPoint.getArgs());

        long threadId = Thread.currentThread().getId();

        RLock lock = redissonClient.getLock(getLockName(joinPoint, lockKey));

        if (!lock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit())) {
            throw new RuntimeException("Lock을 얻는데 실패했습니다.");
        }
        log.info(threadId + " isLocked : " + lock.isLocked());

        Object ret = null;
        try {

            ret = joinPoint.proceed();

        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("{}", e);
        } finally {

            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }

        }

        return ret;
    }

    private String getLockName(ProceedingJoinPoint joinPoint, String lockKey) {
        var methodSig = (MethodSignature) joinPoint.getSignature();
        return methodSig.getMethod().toString() + ":" + lockKey;
    }

}

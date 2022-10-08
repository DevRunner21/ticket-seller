//package com.example.ticketseller;
//
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//import lombok.extern.slf4j.Slf4j;
//import lombok.val;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//@Slf4j
//@Aspect
//@Component // 다른 분산 락 Aspect를 사용하는 경우, 반드시 주석 처리해야 함
//class RedissonDistributedLockAspect(RedissonClient redissonClient) {
//
//    private String uniqueLockMemberIdField = "memberId";
//
//    @Around("@annotation(distributedLock)")
//    public Object executeWithLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
//        String lockName = getLockName(joinPoint);
//        RLock rLock = getLock(lockName);
//        Long waitTime = 0L;
//        Long leaseTime = 0L;
//        TimeUnit timeUnit = null;
//
//        tryLock(
//                rLock = rLock,
//                lockName = lockName,
//                waitTime = distributedLock.waitTime,
//                leaseTime = distributedLock.leaseTime,
//                timeUnit = distributedLock.timeUnit
//        )
//
//        try {
//            return joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        } finally {
//            releaseLock(rLock, lockName);
//        }
//    }
//
//    private String getLockName(ProceedingJoinPoint joinPoint)  {
//        if (ObjectUtils.isEmpty(joinPoint.getArgs())) {
//            throw new IllegalArgumentException("적용하려는 메서드의 인자가 존재하지 않습니다.");
//        }
//
//        return RedissonClientConfig.LOCK_NAME_PREFIX + getMemberId(joinPoint);
//    }
//
//    private Object getMemberId(ProceedingJoinPoint joinPoint) {
//        Arrays.stream(joinPoint.getArgs()).forEach(
//                arg -> {
//                    Field field = getDeclaredMemberIdField(arg);
//                    field.getAnnotation() ?.let {
//                        field.isAccessible = true
//                        return field.get(arg);
//                    }
//
//                }
//        );
//
//        throw RuntimeException("memberId 필드에 @DistributedLockUniqueKey을 설정해주세요.")
//    }
//
//    private Field getDeclaredMemberIdField(Class arg) {
//        try {
//            return arg.getDeclaredField(uniqueLockMemberIdField);
//        } catch (NoSuchFieldException exception) {
//            throw new IllegalAccessException("memberId 필드가 존재하지 않아서 접근할 수 없습니다.");
//        }
//    }
//
//    private RLock getLock(String lockName)  {
//        log.info("getLock (key = {})", lockName);
//
//        return redissonClient.getLock(lockName);
//    }
//
//    private void tryLock(RLock rLock, String lockName, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
//        Boolean isAcquiredLock = false;
//
//        try {
//            isAcquiredLock = rLock.tryLock(waitTime, leaseTime, timeUnit)
//        } catch (InterruptedException exception) {
//            throw new RuntimeException("잠금을 획득하는 과정에서 예외로 인해 작업이 중단되었습니다.");
//        }
//
//        if (!isAcquiredLock) {
//            throw new RuntimeException("일시적으로 작업을 수행할 수 없습니다. 잠시 후에 다시 시도해주세요.");
//        }
//
//        log.info("tryLock (key = {})", lockName)
//    }
//
//    private void releaseLock(RLock rLock, String lockName) {
//        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
//            log.info("releaseLock (key = {})", lockName);
//            rLock.unlock();
//            return;
//        }
//
//        log.info("Already releaseLock (key = {})", lockName)
//    }
//}

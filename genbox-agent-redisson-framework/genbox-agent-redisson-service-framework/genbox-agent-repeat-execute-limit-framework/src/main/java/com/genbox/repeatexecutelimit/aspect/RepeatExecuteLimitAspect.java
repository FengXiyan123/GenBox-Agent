package com.genbox.repeatexecutelimit.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.genbox.constant.LockInfoType;
import com.genbox.exception.GenBoxAgentFrameException;
import com.genbox.handle.RedissonDataHandle;
import com.genbox.locallock.LocalLockCache;
import com.genbox.lockinfo.LockInfoHandle;
import com.genbox.lockinfo.factory.LockInfoHandleFactory;
import com.genbox.repeatexecutelimit.annotion.RepeatExecuteLimit;
import com.genbox.servicelock.LockType;
import com.genbox.servicelock.ServiceLocker;
import com.genbox.servicelock.factory.ServiceLockFactory;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.genbox.repeatexecutelimit.constant.RepeatExecuteLimitConstant.PREFIX_NAME;
import static com.genbox.repeatexecutelimit.constant.RepeatExecuteLimitConstant.SUCCESS_FLAG;

/**
/**
 * 防重复幂等 切面。
 */
@Slf4j
@Aspect
@Order(-11)
@AllArgsConstructor
public class RepeatExecuteLimitAspect {

    private final LocalLockCache localLockCache;

    private final LockInfoHandleFactory lockInfoHandleFactory;

    private final ServiceLockFactory serviceLockFactory;

    private final RedissonDataHandle redissonDataHandle;

    @Around("@annotation(repeatLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatExecuteLimit repeatLimit) throws Throwable {
        long durationTime = repeatLimit.durationTime();
        String message = repeatLimit.message();
        Object obj;
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.REPEAT_EXECUTE_LIMIT);
        String lockName = lockInfoHandle.getLockName(joinPoint,repeatLimit.name(), repeatLimit.keys());
        String repeatFlagName = PREFIX_NAME + lockName;
        String flagObject = redissonDataHandle.get(repeatFlagName);
        if (SUCCESS_FLAG.equals(flagObject)) {
            throw new GenBoxAgentFrameException(message);
        }
        ReentrantLock localLock = localLockCache.getLock(lockName,true);
        boolean localLockResult = localLock.tryLock();
        if (!localLockResult) {
            throw new GenBoxAgentFrameException(message);
        }
        try {
            ServiceLocker lock = serviceLockFactory.getLock(LockType.Fair);
            boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 0);
            if (result) {
                try{
                    flagObject = redissonDataHandle.get(repeatFlagName);
                    if (SUCCESS_FLAG.equals(flagObject)) {
                        throw new GenBoxAgentFrameException(message);
                    }
                    obj = joinPoint.proceed();
                    if (durationTime > 0) {
                        try {
                            redissonDataHandle.set(repeatFlagName,SUCCESS_FLAG,durationTime,TimeUnit.SECONDS);
                        }catch (Exception e) {
                            log.error("getBucket error",e);
                        }
                    }
                    return obj;
                } finally {
                    lock.unlock(lockName);
                }
            }else{
                throw new GenBoxAgentFrameException(message);
            }
        }finally {
            localLock.unlock();
        }
    }
}

package com.genbox.config;

import com.genbox.constant.LockInfoType;
import com.genbox.handle.RedissonDataHandle;
import com.genbox.locallock.LocalLockCache;
import com.genbox.lockinfo.LockInfoHandle;
import com.genbox.lockinfo.factory.LockInfoHandleFactory;
import com.genbox.lockinfo.impl.RepeatExecuteLimitLockInfoHandle;
import com.genbox.repeatexecutelimit.aspect.RepeatExecuteLimitAspect;
import com.genbox.servicelock.factory.ServiceLockFactory;
import org.springframework.context.annotation.Bean;

/**
 * 防重复幂等配置。
 */
public class RepeatExecuteLimitAutoConfiguration {

    @Bean(LockInfoType.REPEAT_EXECUTE_LIMIT)
    public LockInfoHandle repeatExecuteLimitHandle(){
        return new RepeatExecuteLimitLockInfoHandle();
    }

    @Bean
    public RepeatExecuteLimitAspect repeatExecuteLimitAspect(LocalLockCache localLockCache,
                                                             LockInfoHandleFactory lockInfoHandleFactory,
                                                             ServiceLockFactory serviceLockFactory,
                                                             RedissonDataHandle redissonDataHandle){
        return new RepeatExecuteLimitAspect(localLockCache, lockInfoHandleFactory,serviceLockFactory,redissonDataHandle);
    }
}

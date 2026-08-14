package com.genbox.config;

import com.genbox.constant.LockInfoType;
import com.genbox.core.ManageLocker;
import com.genbox.lockinfo.LockInfoHandle;
import com.genbox.lockinfo.factory.LockInfoHandleFactory;
import com.genbox.lockinfo.impl.ServiceLockInfoHandle;
import com.genbox.servicelock.aspect.ServiceLockAspect;
import com.genbox.servicelock.factory.ServiceLockFactory;
import com.genbox.util.ServiceLockTool;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * 分布式锁 配置。
 */
public class ServiceLockAutoConfiguration {

    @Bean(LockInfoType.SERVICE_LOCK)
    public LockInfoHandle serviceLockInfoHandle(){
        return new ServiceLockInfoHandle();
    }

    @Bean
    public ManageLocker manageLocker(RedissonClient redissonClient){
        return new ManageLocker(redissonClient);
    }

    @Bean
    public ServiceLockFactory serviceLockFactory(ManageLocker manageLocker){
        return new ServiceLockFactory(manageLocker);
    }

    @Bean
    public ServiceLockAspect serviceLockAspect(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockAspect(lockInfoHandleFactory,serviceLockFactory);
    }

    @Bean
    public ServiceLockTool serviceLockUtil(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockTool(lockInfoHandleFactory,serviceLockFactory);
    }
}

package com.genbox.core;

import com.genbox.servicelock.LockType;
import com.genbox.servicelock.ServiceLocker;
import com.genbox.servicelock.impl.RedissonFairLocker;
import com.genbox.servicelock.impl.RedissonReadLocker;
import com.genbox.servicelock.impl.RedissonReentrantLocker;
import com.genbox.servicelock.impl.RedissonWriteLocker;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.Map;

import static com.genbox.servicelock.LockType.Fair;
import static com.genbox.servicelock.LockType.Read;
import static com.genbox.servicelock.LockType.Reentrant;
import static com.genbox.servicelock.LockType.Write;

/**
 * 分布式锁 锁缓存。
 */
public class ManageLocker {

    private final Map<LockType, ServiceLocker> cacheLocker = new HashMap<>();

    public ManageLocker(RedissonClient redissonClient){
        cacheLocker.put(Reentrant,new RedissonReentrantLocker(redissonClient));
        cacheLocker.put(Fair,new RedissonFairLocker(redissonClient));
        cacheLocker.put(Write,new RedissonWriteLocker(redissonClient));
        cacheLocker.put(Read,new RedissonReadLocker(redissonClient));
    }

    public ServiceLocker getReentrantLocker(){
        return cacheLocker.get(Reentrant);
    }

    public ServiceLocker getFairLocker(){
        return cacheLocker.get(Fair);
    }

    public ServiceLocker getWriteLocker(){
        return cacheLocker.get(Write);
    }

    public ServiceLocker getReadLocker(){
        return cacheLocker.get(Read);
    }
}

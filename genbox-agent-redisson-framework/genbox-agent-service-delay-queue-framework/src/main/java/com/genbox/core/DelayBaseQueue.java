package com.genbox.core;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

/**
 * 延迟队列 阻塞队列。
 */
@Slf4j
public class DelayBaseQueue {

    protected final RedissonClient redissonClient;
    protected final RBlockingQueue<String> blockingQueue;

    public DelayBaseQueue(RedissonClient redissonClient,String relTopic){
        this.redissonClient = redissonClient;
        this.blockingQueue = redissonClient.getBlockingQueue(relTopic);
    }
}

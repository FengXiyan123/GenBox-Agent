package com.genbox.config;

import com.genbox.lease.RedisLeaseManager;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * 基于 Redis 的服务租约自动配置类。
 */
public class ServiceLeaseAutoConfiguration {

    @Bean
    public RedisLeaseManager redisLeaseManager(RedissonClient redissonClient) {
        return new RedisLeaseManager(redissonClient);
    }
}

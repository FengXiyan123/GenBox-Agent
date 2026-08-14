package com.genbox.context;

import com.genbox.config.DelayQueueProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;

/**
 * 延迟队列配置信息。
 */
@Data
@AllArgsConstructor
public class DelayQueueBasePart {

    private final RedissonClient redissonClient;

    private final DelayQueueProperties delayQueueProperties;
}

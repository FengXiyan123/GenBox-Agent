package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.genbox.enums.BaseCode;
import com.genbox.exception.GenBoxAgentFrameException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * redis配置生成work_id。
 */
public class RedisDisposableWorkerIdAssigner implements WorkerIdAssigner {

    private RedisTemplate redisTemplate;

    public RedisDisposableWorkerIdAssigner (RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long assignWorkerId() {
        String key = "uid_work_id";
        Long increment = redisTemplate.opsForValue().increment(key);
        return Optional.ofNullable(increment).orElseThrow(() -> new GenBoxAgentFrameException(BaseCode.UID_WORK_ID_ERROR));
    }
}

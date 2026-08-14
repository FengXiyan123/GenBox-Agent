package com.genbox.context;

import com.genbox.core.DelayProduceQueue;
import com.genbox.core.IsolationRegionSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列 发送者 分片选择。
 */
public class DelayQueueProduceCombine {

    private final IsolationRegionSelector isolationRegionSelector;

    private final List<DelayProduceQueue> delayProduceQueueList = new ArrayList<>();

    public DelayQueueProduceCombine(DelayQueueBasePart delayQueueBasePart,String topic){
        Integer isolationRegionCount = delayQueueBasePart.getDelayQueueProperties().getIsolationRegionCount();
        isolationRegionSelector =new IsolationRegionSelector(isolationRegionCount);

        for(int i = 0; i < isolationRegionCount; i++) {
            delayProduceQueueList.add(new DelayProduceQueue(delayQueueBasePart.getRedissonClient(),topic + "-" + i));
        }
    }

    public void offer(String content,long delayTime, TimeUnit timeUnit){
        int index = isolationRegionSelector.getIndex();

        delayProduceQueueList.get(index).offer(content, delayTime, timeUnit);
    }
}

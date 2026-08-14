package com.genbox.context;

import com.genbox.core.ConsumerTask;
import lombok.Data;

/**
 * 消息主题。
 */
@Data
public class DelayQueuePart {

    private final DelayQueueBasePart delayQueueBasePart;

    private final ConsumerTask consumerTask;

    public DelayQueuePart(DelayQueueBasePart delayQueueBasePart, ConsumerTask consumerTask){
        this.delayQueueBasePart = delayQueueBasePart;
        this.consumerTask = consumerTask;
    }
}

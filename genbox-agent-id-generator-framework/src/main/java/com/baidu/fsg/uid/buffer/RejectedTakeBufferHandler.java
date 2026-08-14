package com.baidu.fsg.uid.buffer;

/**
 * 处理器。
 */
@FunctionalInterface
public interface RejectedTakeBufferHandler {

    void rejectTakeBuffer(RingBuffer ringBuffer);
}

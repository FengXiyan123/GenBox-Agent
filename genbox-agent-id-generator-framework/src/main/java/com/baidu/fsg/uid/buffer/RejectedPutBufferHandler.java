package com.baidu.fsg.uid.buffer;

/**
 * 处理器。
 */
@FunctionalInterface
public interface RejectedPutBufferHandler {

    void rejectPutBuffer(RingBuffer ringBuffer, long uid);
}

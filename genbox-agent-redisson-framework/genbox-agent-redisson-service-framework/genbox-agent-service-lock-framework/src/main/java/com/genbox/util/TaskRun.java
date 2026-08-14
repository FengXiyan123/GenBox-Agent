package com.genbox.util;

/**
 * 分布式锁 方法类型执行 无返回值的业务。
 */
@FunctionalInterface
public interface TaskRun {

    void run();
}

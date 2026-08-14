package com.genbox.servicelock;

/**
 * 分布式锁 锁类型。
 */
public enum LockType {

    Reentrant,

    Fair,

    Read,

    Write;

    LockType() {
    }

}

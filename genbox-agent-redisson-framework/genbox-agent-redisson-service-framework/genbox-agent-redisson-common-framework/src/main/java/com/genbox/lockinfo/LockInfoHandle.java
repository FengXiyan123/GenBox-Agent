package com.genbox.lockinfo;

import org.aspectj.lang.JoinPoint;

/**
 * 锁信息抽象。
 */
public interface LockInfoHandle {

    String getLockName(JoinPoint joinPoint, String name, String[] keys);

    String simpleGetLockName(String name,String[] keys);
}

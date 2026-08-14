package com.genbox.lockinfo.impl;

import com.genbox.lockinfo.AbstractLockInfoHandle;

/**
 * 锁信息实现(分布式锁)。
 */
public class ServiceLockInfoHandle extends AbstractLockInfoHandle {

    private static final String LOCK_PREFIX_NAME = "SERVICE_LOCK";

    @Override
    protected String getLockPrefixName() {
        return LOCK_PREFIX_NAME;
    }
}

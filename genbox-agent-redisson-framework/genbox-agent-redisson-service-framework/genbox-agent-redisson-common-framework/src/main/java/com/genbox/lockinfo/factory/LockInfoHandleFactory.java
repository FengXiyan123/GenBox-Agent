package com.genbox.lockinfo.factory;

import com.genbox.lockinfo.LockInfoHandle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 锁信息工厂。
 */
public class LockInfoHandleFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public LockInfoHandle getLockInfoHandle(String lockInfoType){
        return applicationContext.getBean(lockInfoType,LockInfoHandle.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

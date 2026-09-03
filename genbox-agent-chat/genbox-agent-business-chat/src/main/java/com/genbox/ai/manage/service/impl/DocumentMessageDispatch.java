package com.genbox.ai.manage.service.impl;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;

/**
 * 将依赖事务数据的消息发布延迟到事务提交之后，避免消费者读到未提交的数据。
 */
final class DocumentMessageDispatch {

    private DocumentMessageDispatch() {
    }

    static void afterCommit(Runnable action) {
        Objects.requireNonNull(action, "action");
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            action.run();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }
}

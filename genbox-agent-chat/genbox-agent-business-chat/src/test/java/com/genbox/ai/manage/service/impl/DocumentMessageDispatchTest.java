package com.genbox.ai.manage.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.transaction.support.TransactionSynchronization.STATUS_ROLLED_BACK;

class DocumentMessageDispatchTest {

    @AfterEach
    void clearSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void dispatchesOnlyAfterTheCurrentTransactionCommits() {
        AtomicBoolean dispatched = new AtomicBoolean();
        TransactionSynchronizationManager.initSynchronization();

        DocumentMessageDispatch.afterCommit(() -> dispatched.set(true));

        assertFalse(dispatched.get());
        TransactionSynchronizationManager.getSynchronizations().forEach(sync -> sync.afterCommit());
        assertTrue(dispatched.get());
    }

    @Test
    void dispatchesImmediatelyWhenNoTransactionIsActive() {
        AtomicBoolean dispatched = new AtomicBoolean();

        DocumentMessageDispatch.afterCommit(() -> dispatched.set(true));

        assertTrue(dispatched.get());
    }

    @Test
    void doesNotDispatchWhenTheTransactionRollsBack() {
        AtomicBoolean dispatched = new AtomicBoolean();
        TransactionSynchronizationManager.initSynchronization();

        DocumentMessageDispatch.afterCommit(() -> dispatched.set(true));

        TransactionSynchronizationManager.getSynchronizations()
            .forEach(sync -> sync.afterCompletion(STATUS_ROLLED_BACK));

        assertFalse(dispatched.get());
    }
}

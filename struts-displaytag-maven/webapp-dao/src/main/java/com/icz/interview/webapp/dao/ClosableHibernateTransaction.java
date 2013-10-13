/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;

import javax.transaction.Synchronization;

/**
 * The session is closed right after {@link org.hibernate.Transaction#commit()}
 * and {@link org.hibernate.Transaction#rollback()}.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public final class ClosableHibernateTransaction implements Transaction {
    private final Transaction transaction;
    private final Session session;

    ClosableHibernateTransaction(Transaction transaction, Session session) {
        this.transaction = transaction;
        this.session = session;
    }

    @Override
    public boolean isInitiator() {
        return transaction.isInitiator();
    }

    @Override
    public void begin() {
        transaction.begin();
    }

    @Override
    public void commit() {
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void rollback() {
        try {
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public LocalStatus getLocalStatus() {
        return transaction.getLocalStatus();
    }

    @Override
    public boolean isActive() {
        return transaction.isActive();
    }

    @Override
    public boolean isParticipating() {
        return transaction.isParticipating();
    }

    @Override
    public boolean wasCommitted() {
        return transaction.wasCommitted();
    }

    @Override
    public boolean wasRolledBack() {
        return transaction.wasRolledBack();
    }

    @Override
    public void registerSynchronization(Synchronization synchronization) throws HibernateException {
        transaction.registerSynchronization(synchronization);
    }

    @Override
    public void setTimeout(int seconds) {
        transaction.setTimeout(seconds);
    }

    @Override
    public int getTimeout() {
        return transaction.getTimeout();
    }
}
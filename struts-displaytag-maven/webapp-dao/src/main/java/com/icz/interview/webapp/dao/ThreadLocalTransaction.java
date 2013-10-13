/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import org.hibernate.Session;

/**
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public final class ThreadLocalTransaction {
    private ThreadLocalTransaction() {}

    public static ClosableHibernateTransaction beginThreadLocalTransaction() {
        HibernateSessionThreadLocal sessions = HibernateSessionThreadLocal.getInstance();
        Session session = sessions.getSession();
        return new ClosableHibernateTransaction(session.beginTransaction(), session);
    }
}
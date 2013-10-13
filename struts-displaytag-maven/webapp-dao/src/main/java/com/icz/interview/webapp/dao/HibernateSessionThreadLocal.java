/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * The ThreadLocal of Hibernate Session. If the session is closed, it is removed from the instance
 * of ThreadLocal. If (re)acquiring the session by {@link #get()}, the (previously closed) session
 * is open by {@link org.hibernate.SessionFactory#openSession()}.
 * Stateless session are not used in this implementation.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
final class HibernateSessionThreadLocal extends ThreadLocal<Session> {
    private static final HibernateSessionThreadLocal instance = new HibernateSessionThreadLocal();

    private HibernateSessionThreadLocal() {}

    public static HibernateSessionThreadLocal getInstance() {
        return instance;
    }

    @Override
    protected Session initialValue() {
        SessionFactory factory = HibernateSessionFactorySingleton.INSTANCE.getFactory();
        return new ReenteredThreadLocalSession(factory.openSession(), this);
    }

    public Session getSession() {
        return get();
    }
}
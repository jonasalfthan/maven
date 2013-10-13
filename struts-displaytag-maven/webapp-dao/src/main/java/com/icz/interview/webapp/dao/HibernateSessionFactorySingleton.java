/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import com.icz.interview.webapp.entities.Project;
import com.icz.interview.webapp.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URL;

/**
 * The purpose of this singleton is to provide Hibernate SessionFactory.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
enum HibernateSessionFactorySingleton {
    INSTANCE;

    private final SessionFactory factory;

    private HibernateSessionFactorySingleton() {
        factory = buildSessionFactory(Project.class, User.class);
    }

    private static SessionFactory buildSessionFactory(Class<?>... annotatedClasses) {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            URL hibernateConfig = Thread.currentThread().getContextClassLoader().getResource("hibernate.cfg.xml");
            Configuration cfg = new Configuration().configure(hibernateConfig);
            for (Class<?> annotatedClass : annotatedClasses) {
                cfg.addAnnotatedClass(annotatedClass);
            }
            return cfg.buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getFactory() {
        return factory;
    }
}
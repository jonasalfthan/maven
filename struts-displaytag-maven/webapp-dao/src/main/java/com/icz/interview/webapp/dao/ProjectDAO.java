/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import com.icz.interview.webapp.entities.Project;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collection;

import static org.hibernate.criterion.Projections.rowCount;

/**
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectDAO implements GenericDAO<Project, Long> {
    private static final HibernateSessionThreadLocal sessions = HibernateSessionThreadLocal.getInstance();

    @Override
    public void drop() throws PersistenceException {
        try {
            for (Project project : selectAll()) {
                delete(project);
            }
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public long count() throws PersistenceException {
        Session session = sessions.getSession();
        try {
            Criteria criteria = session.createCriteria(Project.class);
            criteria = criteria.setProjection(rowCount());
            return (long) criteria.uniqueResult();
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Long insert(Project newInstance) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return (Long) session.save(newInstance);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Project select(Long id) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return (Project) session.get(Project.class, id);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(Project transientObject) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            session.saveOrUpdate(transientObject);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(Project persistentObject) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            session.delete(persistentObject);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Project> selectAll() throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return session.createCriteria(Project.class).list();
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }
}
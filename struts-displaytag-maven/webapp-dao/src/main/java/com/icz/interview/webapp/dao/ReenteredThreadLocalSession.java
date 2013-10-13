/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import org.hibernate.*;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;

import java.io.Serializable;
import java.sql.Connection;

/**
 * Decorator of Session. If closing the session, the session is removed from ThreadLocal as well.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
final class ReenteredThreadLocalSession implements Session {
    private final Session session;
    private final ThreadLocal<Session> sessions;

    ReenteredThreadLocalSession(Session session, ThreadLocal<Session> sessions) {
        this.session = session;
        this.sessions = sessions;
    }

    @Override
    public SharedSessionBuilder sessionWithOptions() {
        return session.sessionWithOptions();
    }

    @Override
    public void flush() throws HibernateException {
        session.flush();
    }

    @Override
    public void setFlushMode(FlushMode flushMode) {
        session.setFlushMode(flushMode);
    }

    @Override
    public FlushMode getFlushMode() {
        return session.getFlushMode();
    }

    @Override
    public void setCacheMode(CacheMode cacheMode) {
        session.setCacheMode(cacheMode);
    }

    @Override
    public CacheMode getCacheMode() {
        return session.getCacheMode();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return session.getSessionFactory();
    }

    @Override
    public Connection close() throws HibernateException {
        sessions.remove();
        return session.close();
    }

    @Override
    public void cancelQuery() throws HibernateException {
        session.cancelQuery();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public boolean isConnected() {
        return session.isConnected();
    }

    @Override
    public boolean isDirty() throws HibernateException {
        return session.isDirty();
    }

    @Override
    public boolean isDefaultReadOnly() {
        return session.isDefaultReadOnly();
    }

    @Override
    public void setDefaultReadOnly(boolean readOnly) {
        session.setDefaultReadOnly(readOnly);
    }

    @Override
    public Serializable getIdentifier(Object object) {
        return session.getIdentifier(object);
    }

    @Override
    public boolean contains(Object object) {
        return session.contains(object);
    }

    @Override
    public void evict(Object object) {
        session.evict(object);
    }

    @Override
    @Deprecated
    public Object load(Class theClass, Serializable id, LockMode lockMode) {
        return session.load(theClass, id, lockMode);
    }

    @Override
    public Object load(Class theClass, Serializable id, LockOptions lockOptions) {
        return session.load(theClass, id, lockOptions);
    }

    @Override
    @Deprecated
    public Object load(String entityName, Serializable id, LockMode lockMode) {
        return session.load(entityName, id, lockMode);
    }

    @Override
    public Object load(String entityName, Serializable id, LockOptions lockOptions) {
        return session.load(entityName, id, lockOptions);
    }

    @Override
    public Object load(Class theClass, Serializable id) {
        return session.load(theClass, id);
    }

    @Override
    public Object load(String entityName, Serializable id) {
        return session.load(entityName, id);
    }

    @Override
    public void load(Object object, Serializable id) {
        session.load(object, id);
    }

    @Override
    public void replicate(Object object, ReplicationMode replicationMode) {
        session.replicate(object, replicationMode);
    }

    @Override
    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        session.replicate(entityName, object, replicationMode);
    }

    @Override
    public Serializable save(Object object) {
        return session.save(object);
    }

    @Override
    public Serializable save(String entityName, Object object) {
        return session.save(entityName, object);
    }

    @Override
    public void saveOrUpdate(Object object) {
        session.saveOrUpdate(object);
    }

    @Override
    public void saveOrUpdate(String entityName, Object object) {
        session.saveOrUpdate(entityName, object);
    }

    @Override
    public void update(Object object) {
        session.update(object);
    }

    @Override
    public void update(String entityName, Object object) {
        session.update(entityName, object);
    }

    @Override
    public Object merge(Object object) {
        return session.merge(object);
    }

    @Override
    public Object merge(String entityName, Object object) {
        return session.merge(entityName, object);
    }

    @Override
    public void persist(Object object) {
        session.persist(object);
    }

    @Override
    public void persist(String entityName, Object object) {
        session.persist(entityName, object);
    }

    @Override
    public void delete(Object object) {
        session.delete(object);
    }

    @Override
    public void delete(String entityName, Object object) {
        session.delete(entityName, object);
    }

    @Override
    @Deprecated
    public void lock(Object object, LockMode lockMode) {
        session.lock(object, lockMode);
    }

    @Override
    @Deprecated
    public void lock(String entityName, Object object, LockMode lockMode) {
        session.lock(entityName, object, lockMode);
    }

    @Override
    public LockRequest buildLockRequest(LockOptions lockOptions) {
        return session.buildLockRequest(lockOptions);
    }

    @Override
    public void refresh(Object object) {
        session.refresh(object);
    }

    @Override
    public void refresh(String entityName, Object object) {
        session.refresh(entityName, object);
    }

    @Override
    @Deprecated
    public void refresh(Object object, LockMode lockMode) {
        session.refresh(object, lockMode);
    }

    @Override
    public void refresh(Object object, LockOptions lockOptions) {
        session.refresh(object, lockOptions);
    }

    @Override
    public void refresh(String entityName, Object object, LockOptions lockOptions) {
        session.refresh(entityName, object, lockOptions);
    }

    @Override
    public LockMode getCurrentLockMode(Object object) {
        return session.getCurrentLockMode(object);
    }

    @Override
    public Query createFilter(Object collection, String queryString) {
        return session.createFilter(collection, queryString);
    }

    @Override
    public void clear() {
        session.clear();
    }

    @Override
    public Object get(Class clazz, Serializable id) {
        return session.get(clazz, id);
    }

    @Override
    @Deprecated
    public Object get(Class clazz, Serializable id, LockMode lockMode) {
        return session.get(clazz, id, lockMode);
    }

    @Override
    public Object get(Class clazz, Serializable id, LockOptions lockOptions) {
        return session.get(clazz, id, lockOptions);
    }

    @Override
    public Object get(String entityName, Serializable id) {
        return session.get(entityName, id);
    }

    @Override
    @Deprecated
    public Object get(String entityName, Serializable id, LockMode lockMode) {
        return session.get(entityName, id, lockMode);
    }

    @Override
    public Object get(String entityName, Serializable id, LockOptions lockOptions) {
        return session.get(entityName, id, lockOptions);
    }

    @Override
    public String getEntityName(Object object) {
        return session.getEntityName(object);
    }

    @Override
    public IdentifierLoadAccess byId(String entityName) {
        return session.byId(entityName);
    }

    @Override
    public IdentifierLoadAccess byId(Class entityClass) {
        return session.byId(entityClass);
    }

    @Override
    public NaturalIdLoadAccess byNaturalId(String entityName) {
        return session.byNaturalId(entityName);
    }

    @Override
    public NaturalIdLoadAccess byNaturalId(Class entityClass) {
        return session.byNaturalId(entityClass);
    }

    @Override
    public SimpleNaturalIdLoadAccess bySimpleNaturalId(String entityName) {
        return session.bySimpleNaturalId(entityName);
    }

    @Override
    public SimpleNaturalIdLoadAccess bySimpleNaturalId(Class entityClass) {
        return session.bySimpleNaturalId(entityClass);
    }

    @Override
    public Filter enableFilter(String filterName) {
        return session.enableFilter(filterName);
    }

    @Override
    public Filter getEnabledFilter(String filterName) {
        return session.getEnabledFilter(filterName);
    }

    @Override
    public void disableFilter(String filterName) {
        session.disableFilter(filterName);
    }

    @Override
    public SessionStatistics getStatistics() {
        return session.getStatistics();
    }

    @Override
    public boolean isReadOnly(Object entityOrProxy) {
        return session.isReadOnly(entityOrProxy);
    }

    @Override
    public void setReadOnly(Object entityOrProxy, boolean readOnly) {
        session.setReadOnly(entityOrProxy, readOnly);
    }

    @Override
    public void doWork(Work work) throws HibernateException {
        session.doWork(work);
    }

    @Override
    public <T> T doReturningWork(ReturningWork<T> work) throws HibernateException {
        return session.doReturningWork(work);
    }

    @Override
    public Connection disconnect() {
        sessions.remove();
        return session.disconnect();
    }

    @Override
    public void reconnect(Connection connection) {
        session.reconnect(connection);
    }

    @Override
    public boolean isFetchProfileEnabled(String name) throws UnknownProfileException {
        return session.isFetchProfileEnabled(name);
    }

    @Override
    public void enableFetchProfile(String name) throws UnknownProfileException {
        session.enableFetchProfile(name);
    }

    @Override
    public void disableFetchProfile(String name) throws UnknownProfileException {
        session.disableFetchProfile(name);
    }

    @Override
    public TypeHelper getTypeHelper() {
        return session.getTypeHelper();
    }

    @Override
    public LobHelper getLobHelper() {
        return session.getLobHelper();
    }

    @Override
    public String getTenantIdentifier() {
        return session.getTenantIdentifier();
    }

    @Override
    public Transaction beginTransaction() {
        return session.beginTransaction();
    }

    @Override
    public Transaction getTransaction() {
        return session.getTransaction();
    }

    @Override
    public Query getNamedQuery(String queryName) {
        return session.getNamedQuery(queryName);
    }

    @Override
    public Query createQuery(String queryString) {
        return session.createQuery(queryString);
    }

    @Override
    public SQLQuery createSQLQuery(String queryString) {
        return session.createSQLQuery(queryString);
    }

    @Override
    public Criteria createCriteria(Class persistentClass) {
        return session.createCriteria(persistentClass);
    }

    @Override
    public Criteria createCriteria(Class persistentClass, String alias) {
        return session.createCriteria(persistentClass, alias);
    }

    @Override
    public Criteria createCriteria(String entityName) {
        return session.createCriteria(entityName);
    }

    @Override
    public Criteria createCriteria(String entityName, String alias) {
        return session.createCriteria(entityName, alias);
    }
}
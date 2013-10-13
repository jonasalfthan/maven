/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import java.io.Serializable;

/**
 * The purpose of this interface is to specify base operations
 * in every DAO implementation.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public interface GenericDAO<T, PK extends Serializable> {
    void drop() throws PersistenceException;
    long count() throws PersistenceException;

    /**
     * Persists the <tt>newInstance</tt> object into database.
     * Primary key is stored in <tt>newInstance</tt>.
     */
    PK insert(T newInstance) throws PersistenceException;

    /**
     * Retrieves an object that was previously persisted to the database
     * using the indicated id as primary key.
     */
    T select(PK id) throws PersistenceException;

    /**
     * Save changes made to a persistent object.
     */
    void update(T transientObject) throws PersistenceException;

    /**
     * Remove an object from persistent storage in the database.
     */
    void delete(T persistentObject) throws PersistenceException;

    /**
     * Retrieves all objects that were previously persisted to the database.
     */
    Iterable<T> selectAll() throws PersistenceException;

    /**
     * Remove an object from persistent storage in the database.
     */
    //void deleteByID(PK id) throws PersistenceException;
}
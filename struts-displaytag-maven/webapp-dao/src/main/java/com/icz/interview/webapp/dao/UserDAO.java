/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import com.icz.interview.webapp.entities.HashAlgorithm;
import com.icz.interview.webapp.entities.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.security.MessageDigest;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class UserDAO implements GenericDAO<User, Long> {
    private static final HibernateSessionThreadLocal sessions = HibernateSessionThreadLocal.getInstance();

    @Override
    public void drop() throws PersistenceException {
        try {
            for (User project : selectAll()) {
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
            Criteria criteria = session.createCriteria(User.class);
            criteria = criteria.setProjection(rowCount());
            return (long) criteria.uniqueResult();
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Long insert(User newInstance) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return (Long) session.save(newInstance);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public User select(Long id) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return (User) session.get(User.class, id);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(User transientObject) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            session.saveOrUpdate(transientObject);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(User persistentObject) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            session.delete(persistentObject);
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<User> selectAll() throws PersistenceException {
        Session session = sessions.getSession();
        try {
            return session.createCriteria(User.class).list();
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public User selectUser(String username, byte[] digest, HashAlgorithm hashAlgorithm) throws PersistenceException {
        Session session = sessions.getSession();
        try {
            String psswdHash = printBase64Binary(digest);
            User u = new User();
            u.setUsername(username);
            u.setPasswordHash(psswdHash);
            u.setHashAlgorithm(hashAlgorithm);

            List results = session.createCriteria(User.class)
                    .add(like("username", username))
                    .add(like("passwordHash", psswdHash))
                    .add(eq("hashAlgorithm", hashAlgorithm))
                    .list();
            return results.size() == 1 ? (User) results.get(0) : null;
        } catch (HibernateException | IllegalArgumentException e) {
            throw new PersistenceException(e);
        }
    }

    public User selectUser(String username, MessageDigest messageDigest) throws PersistenceException {
        HashAlgorithm algorithm = convertHashAlgorithm(messageDigest.getAlgorithm());
        return algorithm != null ? selectUser(username, messageDigest.digest(), algorithm) : null;
    }

    public boolean containsUser(String username, byte[] digest, HashAlgorithm hashAlgorithm) throws PersistenceException {
        return selectUser(username, digest, hashAlgorithm) != null;
    }

    public boolean containsUser(String username, MessageDigest messageDigest) throws PersistenceException {
        HashAlgorithm algorithm = convertHashAlgorithm(messageDigest.getAlgorithm());
        return algorithm != null && containsUser(username, messageDigest.digest(), algorithm);
    }

    public Long insertUser(String username, byte[] digest, HashAlgorithm hashAlgorithm) throws PersistenceException {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(printBase64Binary(digest));
        user.setHashAlgorithm(hashAlgorithm);
        return insert(user);
    }

    public Long insertUser(String username, byte[] digest, MessageDigest messageDigest) throws PersistenceException {
        HashAlgorithm hashAlgorithm = convertHashAlgorithm(messageDigest.getAlgorithm());
        if (hashAlgorithm == null) {
            throw new PersistenceException("Unknown hash algorithm " + messageDigest.getAlgorithm());
        }
        return insertUser(username, digest, hashAlgorithm);
    }

    public Long insertUser(String username, MessageDigest messageDigest) throws PersistenceException {
        return insertUser(username, messageDigest.digest(), messageDigest);
    }

    public boolean verifyHashAlgorithm(String hashAlgorithm) {
        return convertHashAlgorithm(hashAlgorithm) != null;
    }

    public HashAlgorithm convertHashAlgorithm(String hashAlgorithm) {
        try {
            return HashAlgorithm.valueOf(hashAlgorithm.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
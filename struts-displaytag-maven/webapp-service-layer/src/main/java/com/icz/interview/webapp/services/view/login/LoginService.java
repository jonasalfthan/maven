/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.view.login;

import com.icz.interview.webapp.dao.ClosableHibernateTransaction;
import com.icz.interview.webapp.dao.PersistenceException;
import com.icz.interview.webapp.dao.ThreadLocalTransaction;
import com.icz.interview.webapp.dao.UserDAO;
import com.icz.interview.webapp.entities.User;
import com.icz.interview.webapp.services.view.UserDTO;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The purpose of this service class is to perform transaction
 * on data layer when serving the dat to/from projects.jsp.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class LoginService {
    private static final Logger LOG = Logger.getLogger(LoginService.class.getSimpleName());

    public boolean verifyUser(String username, MessageDigest messageDigest) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            UserDAO userDAO = new UserDAO();
            return messageDigest != null && userDAO.containsUser(username, messageDigest);
        } catch (PersistenceException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        } finally {
            transaction.rollback();
        }
    }

    public UserDTO findUser(String username, MessageDigest messageDigest) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            UserDAO userDAO = new UserDAO();
            if (messageDigest != null) {
                User user = userDAO.selectUser(username, messageDigest);
                return user == null ? null : new UserDTO(user);
            } else {
                return null;
            }
        } catch (PersistenceException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } finally {
            transaction.rollback();
        }
    }

    public Long saveUser(String username, MessageDigest messageDigest) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            UserDAO userDAO = new UserDAO();
            Long id = messageDigest != null ? userDAO.insertUser(username, messageDigest) : null;
            transaction.commit();
            return id;
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            transaction.rollback();
            return null;
        }
    }
}
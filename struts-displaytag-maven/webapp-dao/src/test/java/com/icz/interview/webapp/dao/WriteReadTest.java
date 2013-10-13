/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import com.icz.interview.webapp.entities.HashAlgorithm;
import com.icz.interview.webapp.entities.Project;
import com.icz.interview.webapp.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

// general runners annotation

// junit suite runner

// junit categories runner

// junit parametrized runner

// junit runner for use with JUnit 3.8.x-style

// junit inheritable type annotation of suite components

// junit category annotation applied on test methods and/or classes

// junit in/excluded category annotation applied on test suite type

// junit methods' annotations
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

// other junit annotations

// junit field annotations

// junit asserts

import java.util.Collection;

import static org.junit.Assert.*;

// junit assumptions

// junit matchers
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.Is.*;

// junit theories (runner, theory annotation, data-points and annotations and abstract classes)


/**
 * The purpose of this test is to test insertions, selectAll operations of SQL/Hibernate.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public final class WriteReadTest {
    @Rule
    public final TestName test = new TestName();

    @Before
    public void beforeTest() throws PersistenceException {
        System.out.println("[beforeTest]");
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        new ProjectDAO().drop();
        new UserDAO().drop();
        transaction.commit();
    }

    @Test
    public void insertLoadProject() throws Exception {
        System.out.println(test.getMethodName());
        Project insertedProject = new Project();
        insertedProject.setName("n");
        insertedProject.setAbbreviation("a");
        insertedProject.setCustomer("c");
        long insertedProjectId = insertEntity(insertedProject);
        assertThat(insertedProjectId, is(not(0L)));
        Project selectedProject = selectEntity(insertedProjectId, Project.class);
        assertThat(selectedProject, is(insertedProject));
    }

    @Test
    public void insertLoadUser() throws Exception {
        System.out.println(test.getMethodName());
        User insertedUser = new User();
        insertedUser.setUsername("xyz");
        insertedUser.setPasswordHash("0123456789ABCDEF");
        insertedUser.setHashAlgorithm(HashAlgorithm.MD5);
        long insertedUserId = insertEntity(insertedUser);
        assertThat(insertedUserId, is(not(0L)));
        User selectedUser = selectEntity(insertedUserId, User.class);
        assertThat(selectedUser, is(insertedUser));
    }

    @Test
    public void insertLoadProjectDao() throws Exception {
        System.out.println(test.getMethodName());
        Project insertedProject = new Project();
        insertedProject.setName("n");
        insertedProject.setAbbreviation("a");
        insertedProject.setCustomer("c");

        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        ProjectDAO dao = new ProjectDAO();
        long insertedUserId = dao.insert(insertedProject);
        transaction.commit();

        transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        dao = new ProjectDAO();
        Project selectedProject = dao.select(insertedUserId);
        long counter = dao.count();
        Collection<Project> projects = dao.selectAll();
        transaction.rollback();
        assertThat(projects.size(), is(1));

        assertThat(counter, is(1L));
        assertFalse(insertedProject == selectedProject);
        assertThat(selectedProject, is(insertedProject));


        insertedProject = new Project();
        insertedProject.setName("a");
        insertedProject.setAbbreviation("b");
        insertedProject.setCustomer("c");

        transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        dao = new ProjectDAO();
        dao.insert(insertedProject);
        transaction.commit();

        transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        counter = new ProjectDAO().count();
        transaction.rollback();
        assertThat(counter, is(2L));
    }

    private static long insertEntity(Object entity) {
        SessionFactory factory = HibernateSessionFactorySingleton.INSTANCE.getFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        long id = (long) session.save(entity);

        session.getTransaction().commit();
        session.close();

        return id;
    }

    private static <T> T selectEntity(long storedId, Class<T> entityType) throws Exception {
        SessionFactory factory = HibernateSessionFactorySingleton.INSTANCE.getFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        T selectedEntity = entityType.cast(session.get(entityType, storedId));
        selectedEntity = rewrapEntity(selectedEntity, entityType);

        session.getTransaction().commit();
        session.close();

        return selectedEntity;
    }

    private static <T> T rewrapEntity(Object entity, Class<T> entityType) throws Exception {
        return entityType.getConstructor(entityType).newInstance(entity);
    }

}
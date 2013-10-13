/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.view.login;

import com.icz.interview.webapp.dao.*;
import com.icz.interview.webapp.entities.HashAlgorithm;
import com.icz.interview.webapp.services.view.UserDTO;
import org.junit.experimental.theories.suppliers.TestedOn;

// general runners annotation
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

// junit suite runner
import org.junit.runners.Suite;

// junit categories runner
import org.junit.experimental.categories.Categories;

// junit parametrized runner
import org.junit.runners.Parameterized;

// junit runner for use with JUnit 3.8.x-style
import org.junit.runners.AllTests;

// junit inheritable type annotation of suite components
import org.junit.runners.Suite.SuiteClasses;

// junit category annotation applied on test methods and/or classes
import org.junit.experimental.categories.Category;

// junit in/excluded category annotation applied on test suite type
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Categories.ExcludeCategory;

// junit methods' annotations
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;

// other junit annotations
import org.junit.Ignore;

// junit field annotations
import org.junit.Rule;

// junit asserts
import static org.junit.Assert.*;

// junit assumptions
import static org.junit.Assume.*;

// junit matchers
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.AllOf.*;
import static org.hamcrest.core.AnyOf.*;
import static org.hamcrest.core.DescribedAs.*;//todo ?
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsAnything.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsSame.*;

// junit theories (runner, theory annotation, data-points and annotations and abstract classes)
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public final class LoginServiceTest {
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
    public void insertUsersVerifyOne() throws NoSuchAlgorithmException {
        System.out.println(test.getMethodName());
        LoginService service = new LoginService();
        service.saveUser("first user", hashPassword("first password"));
        service.saveUser("second user", hashPassword("second password"));
        service.saveUser("third user", hashPassword("third password"));
        assertNull(service.findUser("any user", hashPassword("any password")));
        assertNull(service.findUser("second user", hashPassword("wrong password")));
        UserDTO userDTO = service.findUser("second user", hashPassword("second password"));
        assertThat(userDTO.getId(), is(not(0L)));
        assertThat(userDTO.getUsername(), is("second user"));
        assertThat(userDTO.getPasswordHash(), is("2ZFeZrxY+fiuD4hTzcX6Wg=="));
        assertThat(userDTO.getHashAlgorithm(), is(HashAlgorithm.MD5));
        assertNotNull(userDTO);
    }

    private static MessageDigest hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(password.getBytes());
        return digest;
    }

}
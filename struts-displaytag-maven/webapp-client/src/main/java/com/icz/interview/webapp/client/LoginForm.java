/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * The model of credentials.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class LoginForm extends StrutsForm<LoginForm> implements Login {
    static final long serialVersionUID = 1;
    private static final String ATTRIBUTE_KEY_USERNAME = "username";
    private static final String ATTRIBUTE_KEY_PASSWORD = "password";
    private static final String ATTRIBUTE_KEY_USERTYPE = "usertype";

    private String username;
    private String password;
    private String usertype;

    public LoginForm() {
        reload(null);
    }

    @Override
    protected void reset(HttpServletRequest request, LoginForm backup) {
        if (backup == null) {
            reload(request);
        } else {
            setUsername(backup.getPassword());
            setPassword(backup.getPassword());
            setUsertype(backup.getUsertype());
        }
    }

    @Override
    protected void reload(HttpServletRequest request) {
        setUsername("");
        setPassword("");
        setUsertype("");
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String username = request.getParameter(ATTRIBUTE_KEY_USERNAME);
        String password = request.getParameter(ATTRIBUTE_KEY_PASSWORD);
        String usertype = request.getParameter(ATTRIBUTE_KEY_USERTYPE);

        ActionErrors error = new ActionErrors();

        if (username == null || username.trim().isEmpty()) {
            error.add(ATTRIBUTE_KEY_USERNAME, new ActionMessage("User name must not be empty!"));
        }

        if (password == null || password.trim().isEmpty()) {
            error.add(ATTRIBUTE_KEY_PASSWORD, new ActionMessage("Password must not be empty!"));
        }

        if (usertype == null || usertype.trim().isEmpty()) {
            error.add(ATTRIBUTE_KEY_USERTYPE, new ActionMessage("User type must not be empty!"));
        }

        return error;
    }
}
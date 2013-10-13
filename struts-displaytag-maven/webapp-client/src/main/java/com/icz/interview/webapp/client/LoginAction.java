/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

/**
 * The method {@link #isLoginAction()} returns {@code true} because this is login action.
 * Credentials is returned as a form, see {@link #authenticate(LoginForm)}.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class LoginAction extends StrutsAction<LoginForm> {

    public LoginAction() {
        super(LoginForm.class);
    }

    @Override
    protected Login authenticate(LoginForm form) {
        return form;
    }

    @Override
    protected boolean isLoginAction() {
        return true;
    }
}
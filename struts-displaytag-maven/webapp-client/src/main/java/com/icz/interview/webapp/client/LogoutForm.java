/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import javax.servlet.http.HttpServletRequest;

/**
 * Logout form does not need any model - null implementation.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class LogoutForm extends StrutsForm<LogoutForm> {
    static final long serialVersionUID = 1;

    @Override
    protected void reset(HttpServletRequest request, LogoutForm backup) {
    }

    @Override
    protected void reload(HttpServletRequest request) {
    }
}
/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;

/**
 * The purpose of this class is to specify the rule when the model is reloaded or reused.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public abstract class StrutsForm<T extends StrutsForm<T>> extends ActionForm {
    protected abstract void reset(HttpServletRequest request, T backup);
    protected abstract void reload(HttpServletRequest request);
}
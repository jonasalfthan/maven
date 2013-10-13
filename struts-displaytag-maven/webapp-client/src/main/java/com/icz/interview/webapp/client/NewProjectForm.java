/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents a new project in JSP in order to save it in database.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class NewProjectForm extends StrutsForm<NewProjectForm> {
    static final long serialVersionUID = 1;

    private String name;
    private String abbreviation;
    private String customer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    boolean isFilledOut() {
        return !isBlank(getName()) && !isBlank(getAbbreviation()) && !isBlank(getCustomer());
    }

    @Override
    protected void reset(HttpServletRequest request, NewProjectForm backup) {
    }

    @Override
    protected void reload(HttpServletRequest request) {
    }

    private static boolean isBlank(String property) {
        return property == null || property.trim().isEmpty();
    }
}
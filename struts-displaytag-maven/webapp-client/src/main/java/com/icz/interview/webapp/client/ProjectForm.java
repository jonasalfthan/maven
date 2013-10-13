/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.ProjectDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The model of project. If it is updated and saved in form, the model becomes dirty.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 * @see #isDirty()
 */
public class ProjectForm extends StrutsForm<ProjectForm> {
    private static final Logger LOG = Logger.getLogger(ProjectForm.class.getSimpleName());
    static final long serialVersionUID = 1;

    private String name;
    private String abbreviation;
    private String customer;
    private boolean isDirty;
    private int rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        isDirty |= areDifferent(name, this.name);
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        isDirty |= areDifferent(abbreviation, this.abbreviation);
        this.abbreviation = abbreviation;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        isDirty |= areDifferent(customer, this.customer);
        this.customer = customer;
    }

    boolean isDirtyAndClear() {
        boolean isDirty = this.isDirty;
        this.isDirty = false;
        return isDirty;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    protected void reset(HttpServletRequest request, ProjectForm backup) {
        if (backup == null) {
            reload(request);
        } else {
            name = backup.getName();
        }
    }

    @Override
    protected void reload(HttpServletRequest request) {
        if (request != null) {
            @SuppressWarnings("unchecked")
            Collection<ProjectDTO> projects = (Collection<ProjectDTO>) request.getSession().getAttribute("PROJECTS");
            if (projects != null && projects.size() >= getRank()) {
                try {
                    ProjectDTO project = ProjectForm.findProjectByRank(projects, getRank());
                    if (project != null) {
                        setName(project.getName());
                        setAbbreviation(project.getAbbreviation());
                        setCustomer(project.getCustomer());
                    }
                } catch (Throwable t) {
                    LOG.log(Level.SEVERE, t.getLocalizedMessage(), t);
                }
            }
        }
    }

    static ProjectDTO findProjectByRank(Collection<ProjectDTO> projects, int rank) {
        for (ProjectDTO project : projects) {
            if (project.getRank() == rank) {
                return project;
            }
        }
        return null;
    }

    private static boolean areDifferent(String s1, String s2) {
        return s1 == null ? s2 != null : !s1.equals(s2);
    }
}
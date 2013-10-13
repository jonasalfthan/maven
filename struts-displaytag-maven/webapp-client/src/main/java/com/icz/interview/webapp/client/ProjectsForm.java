/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.ProjectDTO;
import com.icz.interview.webapp.services.view.projects.ProjectsService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the projects in table of JSP.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectsForm extends StrutsForm<ProjectsForm> {
    private static final long serialVersionUID = 1;

    private final List<ProjectDTO> projects = new ArrayList<>();

    public ProjectsForm() {
        reload(null);
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects.clear();
        if (projects != null) {
            this.projects.addAll(projects);
        }
    }

    @Override
    protected void reset(HttpServletRequest request, ProjectsForm backup) {
        if (backup == null) {
            reload(request);
        } else {
            setProjects(backup.getProjects());
        }
    }

    @Override
    protected void reload(HttpServletRequest request) {
        projects.clear();
        ProjectsService service = new ProjectsService();
        int i = 1;
        for (ProjectDTO project : service.listAllProjects()) {
            project.setRank(i++);
            projects.add(project);
        }
    }
}
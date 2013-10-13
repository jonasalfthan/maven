/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.ProjectDTO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Forwards data into table by forward name "success".
 * If you click on rank (first column in table), the forward-name = "selected".
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectsAction extends StrutsAction<ProjectsForm> {

    public ProjectsAction() {
        super(ProjectsForm.class);
    }

    @Override
    protected boolean isLoginAction() {
        return false;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        ProjectsForm projectsForm = (ProjectsForm) form;
        Collection<ProjectDTO> projects = projectsForm.getProjects();
        request.getSession().setAttribute("PROJECTS", projects);
        return forward;
    }
}
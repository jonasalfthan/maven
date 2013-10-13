/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.ProjectDTO;
import com.icz.interview.webapp.services.view.projects.ProjectsService;
import com.icz.interview.webapp.services.xml.ProjectsXmlService;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Forwards empty form of a new project by forward name "success".
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class NewProjectAction extends StrutsAction<NewProjectForm> {
    private static final long serialVersionUID = 1;
    private static final Logger LOG = Logger.getLogger(NewProjectAction.class.getSimpleName());

    public NewProjectAction() {
        super(NewProjectForm.class);
    }

    @Override
    protected boolean isLoginAction() {
        return false;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        NewProjectForm newProject = (NewProjectForm) form;
        if (isSave(request)) {
            boolean canSave = newProject.isFilledOut();
            return canSave && save(newProject, request) ? mapping.findForward("projects") : mapping.findForward("failure");
        } else {
            return actionForward;
        }
    }

    private boolean save(NewProjectForm form, HttpServletRequest request) {
        try {
            LOG.info("saving a new project");

            ProjectDTO project = new ProjectDTO();
            project.setName(form.getName());
            project.setAbbreviation(form.getAbbreviation());
            project.setCustomer(form.getCustomer());

            ProjectsService projectsService = new ProjectsService();
            ProjectsXmlService projectsXmlService = new ProjectsXmlService();

            projectsService.addProjects(Collections.singleton(project));
            projectsXmlService.writeProjectsXML(projectsService.listAllProjects(), new File("target/projects.xml"));
            return true;
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, t.getLocalizedMessage(), t);
        } finally {
            request.getSession().removeAttribute("PROJECTS");
        }
        return false;
    }

    private static boolean isSave(HttpServletRequest request) {
        return request.getParameter("save") != null;
    }
}
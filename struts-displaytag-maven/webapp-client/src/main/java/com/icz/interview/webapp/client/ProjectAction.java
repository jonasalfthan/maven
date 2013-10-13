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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shows data of one project and may execute a query (if any):
 * <ul>
 *     <li>delete=true</li>
 *     <li>save=true</li>
 * </ul>
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectAction extends StrutsAction<ProjectForm> {
    private static final long serialVersionUID = 1;
    private static final Logger LOG = Logger.getLogger(ProjectAction.class.getSimpleName());

    public ProjectAction() {
        super(ProjectForm.class);
    }

    @Override
    protected boolean isLoginAction() {
        return false;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        return forkPage(actionForward, mapping, request, (ProjectForm) form);
    }

    private ActionForward forkPage(ActionForward actionForward, ActionMapping mapping, HttpServletRequest request, ProjectForm form) {
        if (isDeletion(request)) {
            boolean done = delete(form, request);
            return done ? mapping.findForward("projects") : mapping.findForward("failure");
        } else if (form.isDirtyAndClear() && isSave(request)) {
            boolean done = save(form, request);
            return done ? mapping.findForward("projects") : mapping.findForward("failure");
        } else {
            return actionForward;
        }
    }

    private boolean delete(ProjectForm form, HttpServletRequest request) {
        try {
            LOG.info("query delete");
            @SuppressWarnings("unchecked")
            Collection<ProjectDTO> projects = (Collection<ProjectDTO>) request.getSession().getAttribute("PROJECTS");
            if (projects != null && projects.size() >= form.getRank()) {
                ProjectDTO projectToDelete = ProjectForm.findProjectByRank(projects, form.getRank());
                new ProjectsService().deleteProject(projectToDelete.getId());
                projects.remove(projectToDelete);
                new ProjectsXmlService().writeProjectsXML(projects, new File("target/projects.xml"));
                return true;
            }
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, t.getLocalizedMessage(), t);
        } finally {
            request.getSession().removeAttribute("PROJECTS");
        }
        return false;
    }

    private boolean save(ProjectForm form, HttpServletRequest request) {
        try {
            LOG.info("request header 'save'");
            @SuppressWarnings("unchecked")
            Collection<ProjectDTO> projects = (Collection<ProjectDTO>) request.getSession().getAttribute("PROJECTS");
            if (projects != null && projects.size() >= form.getRank()) {
                try {
                    ProjectDTO project = ProjectForm.findProjectByRank(projects, form.getRank());
                    if (project != null) {
                        project.setName(form.getName());
                        project.setAbbreviation(form.getAbbreviation());
                        project.setCustomer(form.getCustomer());
                        new ProjectsService().updateProject(project);
                        new ProjectsXmlService().writeProjectsXML(projects, new File("target/projects.xml"));
                        return true;
                    }
                } catch (Throwable t) {
                    LOG.log(Level.SEVERE, t.getLocalizedMessage(), t);
                }
            }
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, t.getLocalizedMessage(), t);
        } finally {
            request.getSession().removeAttribute("PROJECTS");
        }
        return false;
    }

    private static boolean isDeletion(HttpServletRequest request) {
        return "delete=true".equals(request.getQueryString());
    }

    private static boolean isSave(HttpServletRequest request) {
        return request.getParameter("save") != null;
    }
}
/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.view.projects;

import com.icz.interview.webapp.dao.ClosableHibernateTransaction;
import com.icz.interview.webapp.dao.PersistenceException;
import com.icz.interview.webapp.dao.ProjectDAO;
import com.icz.interview.webapp.dao.ThreadLocalTransaction;
import com.icz.interview.webapp.entities.Project;
import com.icz.interview.webapp.services.view.ProjectDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The purpose of this service class is to perform transaction
 * on data layer when serving the dat to/from projects.jsp.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectsService {
    private static final Logger LOG = Logger.getLogger(ProjectsService.class.getSimpleName());

    public Collection<ProjectDTO> listAllProjects() {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            ProjectDAO dao = new ProjectDAO();
            Collection<ProjectDTO> projects = new ArrayList<>();
            int rank = 1;
            for (Project project : dao.selectAll()) {
                projects.add(new ProjectDTO(rank++, project));
            }
            return projects;
        } catch (PersistenceException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return Collections.emptySet();
        } finally {
            transaction.rollback();
        }
    }

    public boolean addProjects(Collection<ProjectDTO> projects) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            ProjectDAO dao = new ProjectDAO();
            for (ProjectDTO projectDTO : projects) {
                Project project = new Project();
                project.setName(projectDTO.getName());
                project.setAbbreviation(projectDTO.getAbbreviation());
                project.setCustomer(projectDTO.getCustomer());
                dao.insert(project);
            }
            transaction.commit();
            return true;
        } catch (Throwable e) {
            transaction.rollback();
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean deleteProject(long id) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            Project project = projectDAO.select(id);
            if (project == null) {
                transaction.rollback();
                return false;
            } else {
                projectDAO.delete(project);
                transaction.commit();
                return true;
            }
        } catch (Throwable e) {
            transaction.rollback();
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean updateProject(ProjectDTO projectDto) {
        ClosableHibernateTransaction transaction = ThreadLocalTransaction.beginThreadLocalTransaction();
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            Project project = new Project();
            project.setId(projectDto.getId());
            project.setName(projectDto.getName());
            project.setAbbreviation(projectDto.getAbbreviation());
            project.setCustomer(projectDto.getCustomer());
            projectDAO.update(project);
            transaction.commit();
            return true;
        } catch (Throwable e) {
            transaction.rollback();
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }
}
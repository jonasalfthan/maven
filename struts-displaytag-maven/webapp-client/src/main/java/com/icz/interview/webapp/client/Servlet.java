/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.ProjectDTO;
import com.icz.interview.webapp.services.view.login.LoginService;
import com.icz.interview.webapp.services.view.projects.ProjectsService;
import com.icz.interview.webapp.services.xml.ProjectsXmlService;
import org.apache.struts.action.ActionServlet;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is just trial. It extends Struts' servlet and initializes USER table with credentials.
 * The credentials are user in the authentication.
 *
 * @author tibor17 (Tibor Digana)
 * @version 2.0
 * @since 2.0
 * @see org.apache.struts.action.ActionServlet
 */
public class Servlet extends ActionServlet {
    private static AtomicBoolean LOCK = new AtomicBoolean();

    @Override
    public void init() throws ServletException {
        super.init();
        synchronized (LOCK) {
            if (!LOCK.get()) {
                insertProjects();
                insertUser();
            }
        }
    }

    private static void insertUser() throws ServletException {
        LoginService service = new LoginService();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update("Xzaq123@".getBytes());
            service.saveUser("icz", digest);
            LOCK.set(true);
        } catch (NoSuchAlgorithmException e) {
            throw new ServletException(e);
        }
    }

    private static void insertProjects() {
        ProjectsXmlService xmlService = new ProjectsXmlService();
        Collection<ProjectDTO> projects = xmlService.loadProjects();
        new ProjectsService().addProjects(projects);
        File target = new File("target");
        target.mkdir();
        xmlService.writeProjectsXML(projects, new File(target, "projects.xml"));
    }
}
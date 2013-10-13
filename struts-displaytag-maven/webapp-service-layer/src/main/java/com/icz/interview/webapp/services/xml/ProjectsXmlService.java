/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.xml;

import com.icz.interview.webapp.entities.Project;
import com.icz.interview.webapp.services.view.ProjectDTO;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The purpose of this service class is to manypulate with projects.xml.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectsXmlService {
    private static final Logger LOG = Logger.getLogger(ProjectsXmlService.class.getSimpleName());

    public Collection<ProjectDTO> loadProjects() {
        URL projectsXML = Thread.currentThread().getContextClassLoader().getResource("projects.xml");

        try (InputStream stream = projectsXML.openStream()) {
            JAXBContext context = JAXBContext.newInstance(Projects.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<Projects> element = unmarshaller.unmarshal(new StreamSource(stream), Projects.class);
            Projects projects = element.getValue();
            int rank = 1;
            Collection<ProjectDTO> result = new ArrayList<>();
            for (Project project : projects.getProjects()) {
                result.add(new ProjectDTO(rank++, project));
            }
            return result;
        } catch (IOException | JAXBException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return Collections.emptySet();
        }
    }

    public void writeProjectsXML(Collection<ProjectDTO> projects, File destination) {
        try {
            JAXBContext context = JAXBContext.newInstance(Projects.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            destination.delete();
            destination.createNewFile();
            LOG.info("Created projects.xml file in " + destination.getAbsolutePath());
            FileWriter writer = new FileWriter(destination);
            m.marshal(new JAXBElement<>(new QName("", "projects"), Projects.class, wrapProjects(projects)), writer);
            writer.close();
        } catch (JAXBException | IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private static Projects wrapProjects(Collection<ProjectDTO> projects) {
        Projects wrapper = new Projects();
        List<Project> entities = new ArrayList<>();
        for (ProjectDTO project : projects) {
            Project p = new Project();
            p.setName(project.getName());
            p.setAbbreviation(project.getAbbreviation());
            p.setCustomer(project.getCustomer());
            entities.add(p);
        }
        wrapper.setProjects(entities);
        return wrapper;
    }
}
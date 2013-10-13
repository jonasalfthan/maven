/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.xml;

import com.icz.interview.webapp.entities.Project;
import com.icz.interview.webapp.services.view.ProjectDTO;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JAXB collection of {@link com.icz.interview.webapp.services.view.ProjectDTO}.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
@XmlRootElement(name = "projects")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectsType", propOrder = {"projects"})
public class Projects {

    @XmlElement(name = "project", type = Project.class)
    private List<Project> projects = new ArrayList<>();

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Projects)) return false;

        Projects projects1 = (Projects) o;

        return !(projects != null ? !projects.equals(projects1.projects) : projects1.projects != null);

    }

    @Override
    public int hashCode() {
        return projects != null ? projects.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Projects{");
        sb.append("projects=").append(projects);
        sb.append('}');
        return sb.toString();
    }
}
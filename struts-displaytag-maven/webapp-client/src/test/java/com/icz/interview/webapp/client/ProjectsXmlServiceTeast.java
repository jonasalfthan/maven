/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

// general runners annotation

// junit suite runner

// junit categories runner

// junit parametrized runner

// junit runner for use with JUnit 3.8.x-style

// junit inheritable type annotation of suite components

// junit category annotation applied on test methods and/or classes

// junit in/excluded category annotation applied on test suite type

// junit methods' annotations
import com.icz.interview.webapp.services.view.ProjectDTO;
import com.icz.interview.webapp.services.xml.ProjectsXmlService;
import org.junit.Assert;
import org.junit.Test;

// other junit annotations

// junit field annotations

// junit asserts

// junit assumptions

// junit matchers

// junit theories (runner, theory annotation, data-points and annotations and abstract classes)
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;


/**
 * @author tibor17 (Tibor Digana)
 * @version 2.0
 * @since 2.0
 */
public final class ProjectsXmlServiceTeast {

    @Test
    public void projectsXmlHas5Projects() {
        ProjectsXmlService service = new ProjectsXmlService();
        Collection<ProjectDTO> projects = service.loadProjects();
        assertThat(projects.size(), is(5));
    }

    @Test
    public void writeXmlToFile() throws IOException {
        ProjectsXmlService service = new ProjectsXmlService();
        Collection<ProjectDTO> projects = service.loadProjects();
        File target = new File("target");
        target.mkdir();
        File xml = new File(target, "projects.xml");
        service.writeProjectsXML(projects, xml);
        FileInputStream stream = new FileInputStream(xml);
        assertThat(stream.available(), is(not(0)));
    }


}
/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.entities;

// general runners annotation

// junit suite runner

// junit categories runner

// junit parametrized runner

// junit runner for use with JUnit 3.8.x-style

// junit inheritable type annotation of suite components

// junit category annotation applied on test methods and/or classes

// junit in/excluded category annotation applied on test suite type

// junit methods' annotations
import org.junit.Test;

// other junit annotations

// junit field annotations

// junit asserts
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

// junit assumptions

// junit matchers

// junit theories (runner, theory annotation, data-points and annotations and abstract classes)

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author tibor17
 * @version 0.2
 */
public final class JaxbTransformationTest {

    @Test
    public void marshalAndUnmarshalVerify() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Project.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter w = new StringWriter();

        Project project = new Project();
        project.setName("n");
        project.setAbbreviation("a");
        project.setCustomer("c");

        /*
        Use together with @XmlRootElement on Project
        m.marshal(c, w);
         */
        m.marshal(new JAXBElement<>(new QName("", /*moze byt hocico napr. root*/"project"), Project.class, project), w);
        assertNotNull(w.toString());

        Unmarshaller u = context.createUnmarshaller();
        /* Use together with @XmlRootElement on Project
        c = (MeterInfo) u.unmarshal(new StringReader(w.toString()));
        * */
        JAXBElement<Project> element = u.unmarshal(new StreamSource(new StringReader(w.toString())), Project.class);
        Project meterInfo = element.getValue();

        assertThat(meterInfo, is(project));

        System.out.println(w.toString());
    }

}
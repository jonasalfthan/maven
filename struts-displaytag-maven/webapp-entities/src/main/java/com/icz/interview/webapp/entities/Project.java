/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * The entity of project.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectType", propOrder = {"name", "abbreviation", "customer"})
@Entity(name = "PROJECT")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    public Project() {
    }

    public Project(Project origin) {
        setId(origin.getId());
        setName(origin.getName());
        setAbbreviation(origin.getAbbreviation());
        setCustomer(origin.getCustomer());
    }

    @XmlAttribute(name = "id")
    private long id;

    @XmlElement(required = true, nillable = false)
    private String name;

    @XmlElement(required = true, nillable = false)
    private String abbreviation;

    @XmlElement(required = true, nillable = false)
    private String customer;

    @Basic(optional = false)
    @Column(nullable = false, name = "ID", precision = 20, scale = 0)
    @Id
    @GeneratedValue(generator = "project_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 1, name = "project_id_seq", sequenceName = "project_id_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "NAME")
    @Lob
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "ABBREVIATION")
    @Lob
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "CUSTOMER")
    @Lob
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (id != project.id) return false;
        if (abbreviation != null ? !abbreviation.equals(project.abbreviation) : project.abbreviation != null)
            return false;
        if (customer != null ? !customer.equals(project.customer) : project.customer != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Project{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", abbreviation='").append(abbreviation).append('\'');
        sb.append(", customer='").append(customer).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
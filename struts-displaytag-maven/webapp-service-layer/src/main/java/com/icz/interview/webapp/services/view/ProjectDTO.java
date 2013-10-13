/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.view;

import com.icz.interview.webapp.entities.Project;

import java.io.Serializable;

/**
 * The object is a row in table.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class ProjectDTO implements Serializable {
    private static final long serialVersionUID = 1;

    private int rank;
    private long id;
    private String name;
    private String abbreviation;
    private String customer;

    public ProjectDTO() {
    }

    public ProjectDTO(int rank, long id, String name, String abbreviation, String customer) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.customer = customer;
    }

    public ProjectDTO(int rank, Project project) {
        this(rank, project.getId(), project.getName(), project.getAbbreviation(), project.getCustomer());
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectDTO)) return false;

        ProjectDTO project = (ProjectDTO) o;

        if (abbreviation != null ? !abbreviation.equals(project.abbreviation) : project.abbreviation != null) return false;
        if (customer != null ? !customer.equals(project.customer) : project.customer != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}
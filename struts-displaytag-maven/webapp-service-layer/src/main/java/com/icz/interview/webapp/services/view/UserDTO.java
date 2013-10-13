/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.services.view;

import com.icz.interview.webapp.entities.HashAlgorithm;
import com.icz.interview.webapp.entities.User;

/**
 * The data transfer object of User.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
public class UserDTO {
    private static final long serialVersionUID = 1;

    private long id;
    private String username;
    private String passwordHash;
    private HashAlgorithm hashAlgorithm;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.hashAlgorithm = user.getHashAlgorithm();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }
}
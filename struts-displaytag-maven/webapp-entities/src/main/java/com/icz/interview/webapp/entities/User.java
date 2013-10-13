/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The entity of user. Necessary in credentials.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
@Entity(name = "USER")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String passwordHash;
    private HashAlgorithm hashAlgorithm;

    public User() {
    }

    public User(User origin) {
        setId(origin.getId());
        setUsername(origin.getUsername());
        setPasswordHash(origin.getPasswordHash());
        setHashAlgorithm(origin.getHashAlgorithm());
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "ID", precision = 20, scale = 0)
    @Id
    @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 1, name = "user_id_seq", sequenceName = "user_id_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "USERNAME")
    @Lob
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic(optional = false)
    @Column(nullable = false, name = "PASSWORD_HASH")
    @Lob
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Basic(optional = false)
    @Column(nullable = false, length = 32, name = "HASH_ALGORITHM")
    @Enumerated(EnumType.STRING)
    public HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (hashAlgorithm != user.hashAlgorithm) return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (hashAlgorithm != null ? hashAlgorithm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", passwordHash='").append(passwordHash).append('\'');
        sb.append(", hashAlgorithm=").append(hashAlgorithm);
        sb.append('}');
        return sb.toString();
    }
}
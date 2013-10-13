/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.entities;

/**
 * The purpose of this enum is to encrypt the password.
 *
 * @author tibor17 (Tibor Digana)
 * @version 2.0
 * @since 2.0
 * @see User
 */
public enum HashAlgorithm {
    NULL, MD5, SHA, SHA1
}
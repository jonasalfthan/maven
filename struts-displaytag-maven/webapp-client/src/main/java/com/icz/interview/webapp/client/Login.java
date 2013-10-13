/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

/**
 * Used in returned type {@link StrutsAction#authenticate(StrutsForm)}.
 *
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 * @see StrutsAction#authenticate(StrutsForm)
 */
public interface Login {
    String getUsername();
    String getPassword();
    String getUsertype();
}
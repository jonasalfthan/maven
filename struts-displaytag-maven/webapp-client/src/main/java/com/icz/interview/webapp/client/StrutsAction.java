/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.client;

import com.icz.interview.webapp.services.view.UserDTO;
import com.icz.interview.webapp.services.view.login.LoginService;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * The purpose of this abstract bridge is to control the session lifecycle and simplify a concrete implementation.
 * The concrete action implements {@link #isLoginAction()}. If it is the login action, then the method
 * {@link #authenticate(StrutsForm)} should be overridden and should return the credentials.
 * Every action (except for Logout action) should forward to names: "success", "login", "expired-login".
 * Custom page should be mapped to "success".
 * If the user loads a page in browser after the log out, the "login" forward-name should map to /login.jsp.
 * The session timeout is specified in descriptor. The forward-name "expired-login" maps to /loginExpired.jsp.
 * The login action additionally should use "failed-login".
 * <p/>
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 * @see StrutsForm
 * @see Action
 */
public abstract class StrutsAction<T extends StrutsForm<T>> extends Action {
    private static final String ATTRIBUTE_FORM = "FORM";
    private static final String ATTRIBUTE_ACTION = "ACTION";
    private static final String ATTRIBUTE_ISAUTHORIZED = "ISAUTHORIZED";
    private static final String ATTRIBUTE_USERID = "USERID";
    private static final String WEB_DESCRIPTOR_PARAMETER_PASSWORD_PATTERN = "password-pattern";
    private static final String WEB_DESCRIPTOR_PARAMETER_PASSWORD_ENCRYPTION = "password-encryption-algorithm";
    private static final String FORWARD_NAME_LOGIN = "login";
    private static final String FORWARD_NAME_LOGIN_FAILED = "failed-login";
    private static final String FORWARD_NAME_LOGIN_EXPIRED = "expired-login";
    private static final String FORWARD_NAME_SUCCESS = "success";

    private final Class<T> formClass;

    public StrutsAction(Class<T> formClass) {
        this.formClass = formClass;
    }

    protected abstract boolean isLoginAction();

    protected Login authenticate(T form) {
        return null;
    }

    protected final T loadSessionForm() {
        return formClass.cast(getServlet().getServletContext().getAttribute(ATTRIBUTE_FORM));
    }

    protected final void invalidateSession(HttpSession session) {
        if (session != null) {
            removeForm(session);
            removeAttributes(session);
            session.invalidate();
        }
    }

    protected final Properties getQueries(HttpServletRequest request) {
        Properties queries = new Properties();
        String q = request.getQueryString();
        if (q != null) {
            for (String query : q.split("&")) {
                String[] mapping = query.split("=");
                if (mapping.length == 2) {
                    queries.setProperty(mapping[0], mapping[1]);
                }
            }
        }
        return queries;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return execute(formClass.cast(form), mapping, request);
    }

    private ActionForward execute(T form, ActionMapping mapping, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        if (request.isRequestedSessionIdValid()) {
            if (isLoginAction()) {
                Login login = authenticate(form);
                return forwardWithCredentials(login, mapping, session);
            } else if (isAuthenticated(session)) {
                return forwardWithFormInSession(form, mapping, session, request);
            } else {
                return forwardToLogin(mapping, session);
            }
        } else {
            return forwardWithoutCredentials(mapping, session);
        }
    }

    private ActionForward forwardWithFormInSession(T form, ActionMapping mapping, HttpSession session, HttpServletRequest request) {
        Class<?> previousAction = (Class<?>) session.getAttribute(ATTRIBUTE_ACTION);
        if (previousAction == null || previousAction != getClass()) {
            removeForm(session);
            session.setAttribute(ATTRIBUTE_ACTION, getClass());
            form.reload(request);
            session.setAttribute(ATTRIBUTE_FORM, form);
        } else {
            form.reset(request, formClass.cast(session.getAttribute(ATTRIBUTE_FORM)));
        }
        return mapping.findForward(FORWARD_NAME_SUCCESS);
    }

    private ActionForward forwardWithCredentials(Login login, ActionMapping mapping, HttpSession session) throws NoSuchAlgorithmException {
        boolean isBlankCredentials = login == null || isBlank(login.getUsername()) || isBlank(login.getPassword());
        boolean isSuccessfulLogin = !isBlankCredentials && isValidPassword(login.getPassword()) && existCredentialsInDB(login, session);
        if (isSuccessfulLogin) {
            setValidSession(session);
            session.setAttribute(ATTRIBUTE_ACTION, getClass());
            return mapping.findForward(FORWARD_NAME_SUCCESS);
        } else {
            invalidateSession(session);
            return mapping.findForward(FORWARD_NAME_LOGIN_FAILED);
        }
    }

    private ActionForward forwardWithoutCredentials(ActionMapping mapping, HttpSession session) {
        removeForm(session);
        removeAttributes(session);

        if (isLoginAction()) {
            // this is the first time when the connection is going to be forwarded to login page
            return mapping.findForward(FORWARD_NAME_LOGIN);
        } else {
            return mapping.findForward(FORWARD_NAME_LOGIN_EXPIRED);
        }
    }

    private ActionForward forwardToLogin(ActionMapping mapping, HttpSession session) {
        invalidateSession(session);
        return mapping.findForward(FORWARD_NAME_LOGIN);
    }

    private boolean existCredentialsInDB(Login login, HttpSession session) throws NoSuchAlgorithmException {
        if (login != null && login.getUsername() != null && login.getPassword() != null) {
            String encryptionAlgorithm = getServlet().getInitParameter(WEB_DESCRIPTOR_PARAMETER_PASSWORD_ENCRYPTION);
            MessageDigest md = MessageDigest.getInstance(encryptionAlgorithm);
            md.update(login.getPassword().getBytes());
            String username = login.getUsername();
            LoginService loginService = new LoginService();
            UserDTO userDTO = loginService.findUser(username, md);
            if (userDTO != null) {
                session.setAttribute(ATTRIBUTE_USERID, userDTO.getId());
                return true;
            }
        }
        return false;
    }

    private void setValidSession(HttpSession session) {
        if (session != null) {
            session.setAttribute(ATTRIBUTE_ISAUTHORIZED, Boolean.TRUE);
        }
    }

    private boolean isValidPassword(String pswd) {
        return pswd.matches(getServlet().getInitParameter(WEB_DESCRIPTOR_PARAMETER_PASSWORD_PATTERN));
    }

    private static boolean isAuthenticated(HttpSession session) {
        return session != null && Boolean.TRUE.equals(session.getAttribute(ATTRIBUTE_ISAUTHORIZED));
    }

    private static boolean isBlank(String credentials) {
        return credentials == null || credentials.trim().isEmpty();
    }

    private static void removeAttributes(HttpSession session) {
        if (session != null) {
            session.removeAttribute(ATTRIBUTE_USERID);
            session.removeAttribute(ATTRIBUTE_ISAUTHORIZED);
            session.removeAttribute(ATTRIBUTE_ACTION);
            session.removeAttribute(ATTRIBUTE_FORM);
        }
    }

    private static void removeForm(HttpSession session) {
        if (session != null) {
            Class<?> actionClass = (Class<?>) session.getAttribute(ATTRIBUTE_ACTION);
            if (actionClass != null) {
                session.removeAttribute(actionClass.getSimpleName());
            }
        }
    }
}
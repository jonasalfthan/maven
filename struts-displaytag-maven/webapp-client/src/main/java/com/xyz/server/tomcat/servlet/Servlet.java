/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.xyz.server.tomcat.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The purpose of this servlet is to test the build structure and server.
 * This class will be deleted in the next commit.
 * <p/>
 * @author tibor17 (Tibor Digana)
 * @since 1.0
 * @version 1.0
 */
public class Servlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Hello from Servlet</title></head>");
        out.println("<body><h1>Hello from hello servlet!</h1></body>");
        out.println("</html>");
    }
}

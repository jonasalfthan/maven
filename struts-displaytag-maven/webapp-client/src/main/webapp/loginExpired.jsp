<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<html>
    <head>
        <title>
            <bean:message key="resource.loginExpired.title"/>
        </title>
    </head>
    <body>
        <p alignment="center">
            <font size="+1" color="red">
                <bean:message key="resource.loginExpired.loginExpired"/>
            </font>
        </p>
        <a href="/login.jsp">
            <bean:message key="resource.loginExpired.loginAgain"/>
        </a>
        <br>
        <h2 style="color: red">
            <html:errors/>
        </h2>
    </body>
</html>
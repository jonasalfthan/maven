<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<html>
    <head>
        <title>
            <bean:message key="resource.loginFailed.title"/>
        </title>
    </head>
    <body>
        <p alignment="center">
            <font size="+1" color="red">
                <bean:message key="resource.loginFailed.loginWrong"/>
            </font>
        </p>
        <a href="/login.jsp">
            <bean:message key="resource.loginFailed.loginAgain"/>
        </a>
        <br/>
        <h2 style="color: red">
            <html:errors/>
        </h2>
    </body>
</html>
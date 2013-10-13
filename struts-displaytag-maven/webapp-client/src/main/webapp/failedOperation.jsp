<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
    <head>
        <title>
            <bean:message key="resource.failedOperation.title"/>
        </title>
    </head>
    <body>
        <a href="/projects.do">
            <bean:message key="resource.failedOperation.contentPage.failureReason"/>
        </a>
        <br/>
        <br/>
        <a href="/logout.do">
            <bean:message key="resource.failedOperation.contentPage.logout"/>
        </a>
    </body>
</html>
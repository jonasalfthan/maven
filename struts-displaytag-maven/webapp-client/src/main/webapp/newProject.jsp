<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<html>
<head>
    <title>
        <bean:message key="resource.newProject.title"/>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="css/pages.css" type="text/css"/>
    <link rel="shortcut icon" type="image/x-icon" href="images/appicon/logo.png"/>
</head>
<body>
<div id="container">
    <div id="menu">
        <table alignment="left">
            <tr border="1px solid black" style="background-color: lightgray">
                <td>
                    <img src="images/jsp/settings.png">
                    <bean:message key="resource.newProject.menu.title"/>
                    </img>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="allProjects" style="width: 300px" onclick="window.location.href='/projects.do'">
                        <bean:message key="resource.newProject.menu.showAllProjects"/>
                    </button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="logout" style="width: 300px" onclick="window.location.href='/logout.do'">
                        <bean:message key="resource.newProject.menu.logout"/>
                    </button>
                </td>
            </tr>
        </table>
    </div>
    <div id="content">
        <table align="center">
            <thead>
            <tr border="1px solid black" style="float: right; width: 960px">
                <th valign="top">
                    <bean:message key="resource.newProject.tableHeader"/>
                </th>
            </tr>
            </thead>
            <tr>
                <td>
                    <html:form action="/newProject" method="POST">
                        <table align="left">
                            <tr>
                                <td>
                                    <bean:message key="resource.newProject.contentPage.projectName"/>
                                </td>
                                <td>
                                    <html:text property="name"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <bean:message key="resource.newProject.contentPage.projectAbbreviation"/>
                                </td>
                                <td>
                                    <html:text property="abbreviation"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <bean:message key="resource.newProject.contentPage.projectCustomer"/>
                                </td>
                                <td>
                                    <html:text property="customer"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <!-- request parameter = save -->
                                    <html:submit property="save">
                                        <bean:message key="resource.newProject.contentPage.submit.btn"/>
                                    </html:submit>
                                </td>
                            </tr>
                        </table>
                    </html:form>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
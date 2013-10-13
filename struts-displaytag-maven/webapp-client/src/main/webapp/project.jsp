<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<html>
    <head>
        <title>
            <bean:message key="resource.project.title"/>
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
                                <bean:message key="resource.project.menu.title"/>
                            </img>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="delete" style="width: 300px" onclick="window.location.href='/project.do?delete=true'">
                                <bean:message key="resource.project.menu.delete"/>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="allProjects" style="width: 300px" onclick="window.location.href='/projects.do'">
                                <bean:message key="resource.project.menu.showAllProjects"/>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="logout" style="width: 300px" onclick="window.location.href='/logout.do'">
                                <bean:message key="resource.project.menu.logout"/>
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
                                <bean:message key="resource.project.tableHeader"/>
                            </th>
                        </tr>
                    </thead>
                    <tr>
                        <td>
                            <html:form action="/project" method="POST">
                                <table align="left">
                                    <tr>
                                        <td>
                                            <bean:message key="resource.project.contentPage.projectName"/>
                                        </td>
                                        <td>
                                            <html:text property="name"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <bean:message key="resource.project.contentPage.projectAbbreviation"/>
                                        </td>
                                        <td>
                                            <html:text property="abbreviation"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <bean:message key="resource.project.contentPage.projectCustomer"/>
                                        </td>
                                        <td>
                                            <html:text property="customer"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <!-- request parameter = save -->
                                            <html:submit property="save">
                                                <bean:message key="resource.project.contentPage.submit.btn"/>
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
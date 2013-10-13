<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
    <head>
        <title>
            <bean:message key="resource.projects.title"/>
        </title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <link rel="stylesheet" href="css/displaytag.css" type="text/css"/>
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
                                <bean:message key="resource.projects.menu.title"/>
                            </img>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="newProject" style="width: 300px" onclick="window.location.href='/newProject.do'">
                                <bean:message key="resource.projects.menu.newProject"/>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="Logout" style="width: 300px" onclick="window.location.href='/logout.do'">
                                <bean:message key="resource.projects.menu.logout"/>
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
                                <h2>
                                    <bean:message key="resource.projects.tableHeader"/>
                                </h2>
                            </th>
                        </tr>
                    </thead>
                    <tr>
                        <td>
                            <h4>
                                <bean:message key="resource.projects.contentPage"/>
                            </h4>
                            <display:table id="data" export="true" name="sessionScope.ProjectsForm.projects" requestURI="/projects.do" pagesize="10">
                                <display:column property="rank" title="Rank" media="html" sortable="true" escapeXml="true" paramId="rank" href="/project.do"/>
                                <display:column property="name" title="Name" media="all" sortable="true" escapeXml="true"/>
                                <display:column property="abbreviation" title="Abbreviation" media="all" sortable="true" escapeXml="true"/>
                                <display:column property="customer" title="Customer" media="all" sortable="true" escapeXml="true"/>
                            </display:table>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
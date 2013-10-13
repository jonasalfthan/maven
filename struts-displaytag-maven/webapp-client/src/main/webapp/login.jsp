<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page contentType="text/html;charset=ISO-8859-1" language="java"%>
<html>
    <head>
        <title><bean:message key="resource.login.title"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <link rel="shortcut icon" type="image/x-icon" href="images/appicon/logo.png"/>
        <script language="JavaScript">
            function validate(objectForm) {
                if (objectForm.username.value.length == 0) {
                    objectForm.username.focus();
                    alert('<bean:message key="alert.login.username"/>');
                    return false;
                }

                if (objectForm.password.value.length == 0) {
                    objectForm.password.focus();
                    alert('<bean:message key="alert.login.password"/>');
                    return false;
                }

                if (objectForm.usertype.selectedIndex == 0 || objectForm.usertype.value.trim() == '') {
                    objectForm.usertype.focus();
                    alert('<bean:message key="alert.login.usertype"/>');
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <html:form action="/login" method="POST" onsubmit="return validate(this);">
            <table align="center">
                <tr>
                    <td><bean:message key="resource.login.username"/> </td>
                    <td><html:text property="username" style="width: 150px"/></td>
                </tr>
                <tr>
                    <td><bean:message key="resource.login.password"/> </td>
                    <td><html:password property="password" style="width: 150px"/></td>
                </tr>
                <tr>
                    <td><bean:message key="resource.login.usertype"/> </td>
                    <td>
                        <html:select property="usertype" style="width: 150px">
                            <html:option value="                                       "/>
                            <html:option value="USER"/>
                            <html:option value="ADMINISTRATOR"/>
                        </html:select>
                    </td>
                    <td>
                        <html:submit><bean:message key="resource.login.submit.btn"/></html:submit>
                    </td>
                </tr>
            </table>
        </html:form>

        <br>
        <h2 style="color: red"><html:errors/></h2>
    </body>
</html>
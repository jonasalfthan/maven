1. Use Java 1.7 or higher.

2. How to build the project
mvn package

3. Build Of Material
server-tomcat7/target/*.zip
extract and launch batch file to start the server: start.bat and start.sh

4. What is the User Name and Password?
User Name=icz
Password=Xzaq123@

5. Where to find the H2 database and projects.xml file being modified?
If you run the server in IDE (Eclipse, IDEA or NetBeans) go to the root directory of installed Tomcat server.
Then you will find the directory /bin/target and files: test.h2.db, test.trace.db, projects.xml
If you start the from the batch file, theH2 database files and projects.xml are located in ${userdir}/target.
The ${userdir} is the directory where the batch/script file is located.

6. How to recreate the IntelliJ IDEA project files
mvn com.googlecode:maven-idea-plugin:1.6:idea
or launch the create-idea-project.bat file.
Note: Make sure the system variable M2_HOME is specified, e.g.:
on Windows
set /p M2_HOME=c:\cygwin\usr\local\etc\junit.mavenize3\build\maven\apache-maven-3.0.4
or on Linux
export M2_HOME=c:\cygwin\usr\local\etc\junit.mavenize3\build\maven\apache-maven-3.0.4

7. Development Conventions
Import /src/main/config/settings.xml in your IDEA project to admit the unique code structure and file templates.
The headers in Java files must contain license. See the file LICENSE.txt.
The JavaDoc in class files should update the following template:
/**
 * The purpose of this interface/class/annotation/enum is to ...
 * <p/>
 * Use cases ...
 * <p/>
 * The constraints ...
 * <p/>
 * @author ${USER}
 * @since ${VERSION}
 * @version ${VERSION}
 * @see ${SEE}
 */
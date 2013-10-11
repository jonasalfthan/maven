
%JAVA_HOME%\bin\java -version
rem The JDK's permanent memory MaxPermSize behaves differently depending on whether a debugging is enable, i.e. if there is an active agent. 
rem If there is an active agent, the JDK can fail to collect permanent memory in some cases. (Specifically, when some code introspects a class with a primitive array like, byte[] or char[].) This can cause the permanent space to run out, for example, when redeploying .war files. 


@echo Build All

del /f /s *.iml
del /f /s *.ipr
del /f /s *.iws

call mvn.bat --errors --fail-fast com.googlecode:maven-idea-plugin:1.6:idea
GOTO END

:END
@echo off
pause | echo Press any key to exit. . .
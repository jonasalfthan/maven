#!/bin/sh

java -Xms512m -Xmx752m -XX:MaxPermSize=512m -XX:+AggressiveHeap -Djdbc.datasource.name=h2database -jar ${pom.artifactId}-${project.version}-war-exec.jar -httpPort=${webapp.port} -resetExtract -extractDirectory ./extract
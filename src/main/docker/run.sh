#!/bin/bash
JAVA_OPTS=${JAVA_OPTS:-'-Xms16m -Xmx64m'}
java $JAVA_OPTS -jar /${project.build.finalName}.jar
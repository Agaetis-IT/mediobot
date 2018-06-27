FROM openjdk:8
ADD /${project.build.finalName}.jar /
ADD /run.sh /
EXPOSE 8080
ENTRYPOINT ["bash", "/run.sh"]

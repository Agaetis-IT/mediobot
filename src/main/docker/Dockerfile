FROM openjdk:8
ADD /${project.build.finalName}.jar /
ADD /run.sh /
EXPOSE 80
ENTRYPOINT ["bash", "/run.sh"]

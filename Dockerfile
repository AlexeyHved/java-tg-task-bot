FROM amazoncorretto:17-alpine-jdk
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9192
COPY target/*.jar task-bot.jar
#ENTRYPOINT ["java", "-jar", "/task-bot.jar"]

FROM openjdk:17-jdk

EXPOSE 8080

ADD ./build/libs/config-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
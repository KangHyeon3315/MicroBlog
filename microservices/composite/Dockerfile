FROM openjdk:17-jdk

EXPOSE 8888

ADD ./build/libs/composite-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM openjdk:17-jdk

EXPOSE 8080

EXPOSE 443/tcp

ADD ./build/libs/gateway-service-0.0.1-SNAPSHOT.jar app.jar

COPY ./keystore.p12 /keystore.p12

ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 7001

ENTRYPOINT ["java","-jar","app.jar"]
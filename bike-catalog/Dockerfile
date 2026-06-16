FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/bike-catalog-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 7002

ENTRYPOINT ["java","-jar","app.jar"]
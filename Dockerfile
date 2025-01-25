FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.5_11-jre-alpine-3.21 AS runner
WORKDIR /pokedex

COPY --from=builder /app/target/*.jar ./runner.jar 

EXPOSE 5000

CMD ["java", "-jar", "runner.jar"]

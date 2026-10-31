FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre

# ↓↓↓ ДОБАВИТЬ ЭТИ 2 СТРОКИ ↓↓↓
RUN groupadd -r appuser && useradd -r -g appuser -u 1000 appuser
RUN mkdir -p /app/logs && chown -R appuser:appuser /app
# ↑↑↑ ДОБАВИТЬ ЭТИ 2 СТРОКИ ↑↑↑

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# ↓↓↓ И ЭТУ 1 СТРОКУ ↓↓↓
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
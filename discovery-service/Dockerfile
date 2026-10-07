# Этап 1: Сборка из общего корня проекта
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app

# 1. Копируем все модули
COPY pom.xml .
COPY user-service/pom.xml ./user-service/
COPY notification-service/pom.xml ./notification-service/
COPY user-notification-common/pom.xml ./user-notification-common/
COPY gateway-service/pom.xml ./gateway-service/
COPY discovery-service/pom.xml ./discovery-service/

# 2.Копируем код сервиса
COPY discovery-service/src ./discovery-service/src

# 3. Собираем модуль
RUN mvn -pl discovery-service -am clean package -DskipTests

# Этап 2: Финальный легковесный образ для запуска
FROM eclipse-temurin:26
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring /app/discovery-service/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "app.jar"]
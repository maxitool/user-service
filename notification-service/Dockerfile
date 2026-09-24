# Этап 1: Сборка из общего корня проекта
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Копируем родительский pom.xml и pom-файлы ВСЕХ модулей
COPY pom.xml .
COPY user-service/pom.xml ./user-service/
COPY notification-service/pom.xml ./notification-service/
COPY user-notification-common/pom.xml ./user-notification-common/

# 2. ИСПРАВЛЕНО: Копируем код сервиса И код общего модуля
COPY user-notification-common/src ./user-notification-common/src
COPY notification-service/src ./notification-service/src

# 3. Собираем модуль notification-service (зависимость соберется автоматически)
RUN mvn -pl notification-service -am clean package -DskipTests

# Этап 2: Финальный легковесный образ для запуска
FROM eclipse-temurin:26
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring /app/notification-service/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "app.jar"]
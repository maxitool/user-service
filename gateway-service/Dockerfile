# Сборка из общего корня проекта
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app

# Копируем все модули
COPY pom.xml .
COPY user-service/pom.xml ./user-service/
COPY notification-service/pom.xml ./notification-service/
COPY user-notification-common/pom.xml ./user-notification-common/
COPY gateway-service/pom.xml ./gateway-service/
COPY discovery-service/pom.xml ./discovery-service/
COPY config-service/pom.xml ./config-service/

# Копируем код сервиса
COPY gateway-service/src ./gateway-service/src

# Собираем модуль
RUN mvn -pl gateway-service -am clean package -DskipTests

# Финальный легковесный образ для запуска
FROM eclipse-temurin:26
WORKDIR /app

# Устанавливаем curl для healthcheck (до создания non-root пользователя!)
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

RUN groupadd --system spring && useradd --system --gid spring spring

# create the mount point and give it to the app user
RUN mkdir -p /app/shared-logs && chown spring:spring /app/shared-logs

COPY --from=build --chown=spring:spring /app/gateway-service/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "app.jar"]
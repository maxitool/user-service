# Сборка из общего корня проекта
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app

# Копируем модули
COPY pom.xml .
COPY discovery-service/pom.xml ./discovery-service/
COPY config-service/pom.xml ./config-service/
COPY gateway-service/pom.xml ./gateway-service/
COPY user-service/pom.xml ./user-service/
COPY notification-service/pom.xml ./notification-service/
COPY user-notification-common/pom.xml ./user-notification-common/

# Скачиваем все зависимости внутрь слоя Docker
RUN mvn dependency:go-offline -pl discovery-service -am

# Копируем код сервиса
COPY discovery-service/src ./discovery-service/src

# Собираем модуль
RUN mvn -pl discovery-service -am clean package -DskipTests

# Финальный легковесный образ для запуска
FROM eclipse-temurin:26
WORKDIR /app

# Устанавливаем curl для healthcheck, создание non-root пользователя
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/* && \
    groupadd --system spring && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring /app/discovery-service/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "app.jar"]
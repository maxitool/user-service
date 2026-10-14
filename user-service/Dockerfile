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

# Копируем код сервиса И код общего модуля (он нужен для компиляции)
COPY user-notification-common/src ./user-notification-common/src
COPY user-service/src ./user-service/src

# Скачиваем все зависимости внутрь слоя Docker
RUN mvn dependency:go-offline -pl user-service -am

# Собираем модуль
RUN mvn -pl user-service -am clean package -DskipTests

# Финальный легковесный образ для запуска
FROM eclipse-temurin:26
WORKDIR /app

# Устанавливаем curl для healthcheck, создание non-root пользователя и папки логов
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/* && \
    groupadd --system spring && useradd --system --gid spring spring && \
    mkdir -p /app/shared-logs && chown spring:spring /app/shared-logs

COPY --from=build --chown=spring:spring /app/user-service/target/*.jar app.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "app.jar"]
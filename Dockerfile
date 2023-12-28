FROM maven:3.8.5-openjdk-17-slim AS build
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package -Pproduction -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=build /target/*.jar application.jar

#добавлено для того, что бы выгрузка в excel работала на образе alpine
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]

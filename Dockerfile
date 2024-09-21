FROM gradle:8.1-jdk17-alpine as build

ENV APP_HOME=/company
WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradlew $APP_HOME/
COPY gradle $APP_HOME/gradle
RUN chmod +x ./gradlew
RUN ./gradlew build -x test || return 0

COPY src $APP_HOME/src
RUN gradle clean build -x test

# Stage 2: Run stage
FROM openjdk:17-alpine
ENV APP_HOME=/company

# Arguments
ARG ARTIFACT_NAME=company.jar
ARG JAR_FILE_PATH=build/libs/companyInfo-0.0.1-SNAPSHOT.jar
ARG PROFILE


# Environment variables
ENV SPRING_PROFILES_ACTIVE=${PROFILE}
ENV JAVA_TOOL_OPTIONS="-XX:MinRAMPercentage=90 -XX:MaxRAMPercentage=90"

# Working directory
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/$JAR_FILE_PATH $ARTIFACT_NAME

## Entrypoint
ENTRYPOINT ["java", "-jar", "company.jar"]
FROM maven:3.8-openjdk-17 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 mvn verify --fail-never
ADD ./src $HOME/src
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM openjdk:17-jdk-slim
COPY --from=build /usr/app/target/auth-service-1.jar /app/runner.jar
ENTRYPOINT java -jar /app/runner.jar
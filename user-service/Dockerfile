FROM shared_lib as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 mvn verify --fail-never
ADD ./src $HOME/src
RUN --mount=type=cache,target=/root/.m2 mvn package
RUN --mount=type=cache,target=/root/.m2 mvn liquibase:clearCheckSums -Dliquibase.url=jdbc:postgresql://localhost:5432/users_service -Dliquibase.username=postgres -Dliquibase.password=postgres_password

FROM openjdk:17-jdk-slim
COPY --from=build /usr/app/target/user-service-1.jar /app/runner.jar
ENTRYPOINT java -jar /app/runner.jar
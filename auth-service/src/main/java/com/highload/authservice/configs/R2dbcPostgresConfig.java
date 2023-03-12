package com.highload.authservice.configs;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class R2dbcPostgresConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.database:auth-service}")
    String postgresDatabase;

    @Value("${spring.r2dbc.host:localhost}")
    String postgresHost;

    @Value("${spring.r2dbc.port:5433}")
    int postgresPort;

    @Value("${spring.r2dbc.username}")
    String postgresUsername;

    @Value("${spring.r2dbc.password}")
    String postgresPassword;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(postgresHost)
                .port(postgresPort)
                .username(postgresUsername)
                .password(postgresPassword)
                .database(postgresDatabase)
                .build()
        );
    }
}

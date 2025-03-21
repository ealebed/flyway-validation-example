package com.example.db;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class FlywayMigrationTest {
    private static final String DOCKER_IMAGE = "postgres:17";
    private static final String DB_NAME = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String INIT_SCRIPT_PATH = "initialize/initdb.sql";

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withInitScript(INIT_SCRIPT_PATH);

    @ParameterizedTest
    @ValueSource(strings = {
        "database_01",
        "database_02"
    })
    public void runMigrations(String databaseName) {
        container.withDatabaseName(databaseName);
        var flyway = Flyway.configure()
            .locations("migrations/%s/".formatted(databaseName))
            .schemas("public")
            .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
            .load();
        flyway.info();
        flyway.migrate();
    }
}

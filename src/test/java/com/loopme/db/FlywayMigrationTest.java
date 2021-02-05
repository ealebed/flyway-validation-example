package com.loopme.db;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class FlywayMigrationTest {
    private static final String DOCKER_IMAGE = "postgres:13";
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

    @Test
    public void runDB1Migrations() {
        container.withDatabaseName("database_01");
        var flyway = Flyway.configure()
                .locations("migrations/database_01/")
                .schemas("public")
                .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
                .load();
        flyway.info();
        flyway.migrate();
    }

    @Test
    public void runDB2Migrations() {
        container.withDatabaseName("database_02");
        var flyway = Flyway.configure()
                .locations("migrations/database_02/")
                .schemas("public")
                .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
                .load();
        flyway.info();
        flyway.migrate();
    }
}

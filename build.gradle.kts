plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val flywayVersion = "11.0.0"
    val testcontainersVersion = "1.20.4"
    val jupiterVersion = "5.11.3"

    testImplementation("org.junit.jupiter:junit-jupiter:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.flywaydb:flyway-core:${flywayVersion}")
    testImplementation("org.flywaydb:flyway-database-postgresql:${flywayVersion}")
    testImplementation("org.postgresql:postgresql:42.7.4")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

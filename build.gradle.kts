plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val flywayVersion = "10.8.1"
    val testcontainersVersion = "1.19.5"
    val jupiterVersion = "5.10.2"

    testImplementation("org.junit.jupiter:junit-jupiter:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.flywaydb:flyway-core:${flywayVersion}")
    testImplementation("org.flywaydb:flyway-database-postgresql:${flywayVersion}")
    testImplementation("org.postgresql:postgresql:42.7.1")
    testImplementation("org.slf4j:slf4j-simple:2.0.12")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

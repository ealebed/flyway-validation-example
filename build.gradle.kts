plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val flywayVersion = "11.3.3"
    val testcontainersVersion = "1.20.5"
    val jupiterVersion = "5.11.4"

    testImplementation("org.junit.jupiter:junit-jupiter:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.flywaydb:flyway-core:${flywayVersion}")
    testImplementation("org.flywaydb:flyway-database-postgresql:${flywayVersion}")
    testImplementation("org.postgresql:postgresql:42.7.5")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

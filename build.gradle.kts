plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    val testcontainersVersion = "1.16.0"

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
    testImplementation("org.flywaydb:flyway-core:7.14.1")
    testImplementation("org.postgresql:postgresql:42.2.23")
    testImplementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

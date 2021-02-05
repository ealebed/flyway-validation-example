plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    val testcontainersVersion = "1.15.1"

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.flywaydb:flyway-core:7.5.0")
    testImplementation("org.postgresql:postgresql:42.2.18")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

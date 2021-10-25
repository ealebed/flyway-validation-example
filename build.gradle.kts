plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    val testcontainersVersion = "1.16.2"

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.flywaydb:flyway-core:8.0.1")
    testImplementation("org.postgresql:postgresql:42.2.24")
    testImplementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

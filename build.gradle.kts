plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    val testcontainersVersion = "1.17.3"

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.flywaydb:flyway-core:9.3.1")
    testImplementation("org.postgresql:postgresql:42.5.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.2")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

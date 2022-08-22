plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    val testcontainersVersion = "1.17.3"

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.flywaydb:flyway-core:9.1.3")
    testImplementation("org.postgresql:postgresql:42.4.2")
    testImplementation("org.slf4j:slf4j-simple:1.7.36")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

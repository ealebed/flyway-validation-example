plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val testcontainersVersion = "1.18.3"
    val jupiterVersion = "5.9.3"

    testImplementation("org.junit.jupiter:junit-jupiter:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.flywaydb:flyway-core:9.20.0")
    testImplementation("org.postgresql:postgresql:42.6.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.7")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val testcontainersVersion = "1.19.1"
    val jupiterVersion = "5.10.1"

    testImplementation("org.junit.jupiter:junit-jupiter:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.flywaydb:flyway-core:9.22.3")
    testImplementation("org.postgresql:postgresql:42.6.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.9")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:postgresql:${testcontainersVersion}")
    testImplementation("org.testcontainers:testcontainers:${testcontainersVersion}")
}

tasks.test {
    useJUnitPlatform()
}

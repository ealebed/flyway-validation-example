plugins {
    java
    id("com.diffplug.spotless") version "8.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    val flywayVersion = "12.0.1"
    val testcontainersVersion = "2.0.3"
    val jupiterVersion = "6.0.2"
    val platformVersion = "6.0.2"

    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    testImplementation("org.postgresql:postgresql:42.7.10")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$platformVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks.test {
    useJUnitPlatform()
}

// Spotless configuration for code formatting
spotless {
    java {
        target("src/**/*.java")
        // Use Google Java Format (default version in Spotless 8.x is 1.28.0)
        googleJavaFormat("1.28.0")
        // Remove unused imports
        removeUnusedImports()
        // Format license header (optional - uncomment if you want to enforce license headers)
        // licenseHeader("/* Licensed under Apache-2.0 */")
    }
    kotlin {
        target("**/*.kts")
        // Use ktlint 1.7.1 (default for Spotless 8.x)
        ktlint("1.7.1")
    }
}

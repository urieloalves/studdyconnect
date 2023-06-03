plugins {
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

group = "dev.urieloalves.studyconnect.application"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.0")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("com.auth0:java-jwt:4.4.0")

    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
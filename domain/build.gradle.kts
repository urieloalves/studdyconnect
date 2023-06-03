plugins {
    kotlin("jvm") version "1.8.21"
}

group = "dev.urieloalves.studyconnect.domain"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
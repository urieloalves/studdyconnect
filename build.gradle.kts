plugins {
    kotlin("jvm") version "1.8.21"
}

group = "dev.urieloalves.studyconnect"
version = "0.0.1"


repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}
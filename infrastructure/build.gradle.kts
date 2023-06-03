plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("org.flywaydb.flyway") version "9.17.0"
}

group = "dev.urieloalves.studyconnect.infrastructure"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("io.github.cdimascio:dotenv-java:2.2.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.flywaydb:flyway-core:9.17.0")
    implementation("dev.kord:kord-core:0.9.0")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.0")
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.0")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.0")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.0")
    implementation("io.ktor:ktor-server-swagger:2.3.0")
    implementation("io.ktor:ktor-server-openapi:2.3.0")
    implementation("io.ktor:ktor-server-status-pages:2.3.0")
    implementation("io.ktor:ktor-server-request-validation:2.3.0")
    implementation("io.ktor:ktor-client-core:2.3.0")
    implementation("io.ktor:ktor-client-cio:2.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.0")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.0")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.0")

    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

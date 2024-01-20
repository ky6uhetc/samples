plugins {
    kotlin("jvm") version "1.9.21"
}

group = "ru.futurio" // futurio.ru/architectures_and_design_patterns
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "1.9.21"
    id("jacoco")
    id("io.gitlab.arturbosch.detekt").version("1.23.0")
}

group = "ru.futurio" // futurio.ru/architectures_and_design_patterns
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}


tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
    ignoreFailures = true
    reports {
        html.required.set(false)
        xml.required.set(true) // checkstyle like format for integration with Jenkins
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

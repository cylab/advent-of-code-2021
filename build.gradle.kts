import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "me.mhenze"
version = "1.0-SNAPSHOT"

// to make files beneath the class loadable
sourceSets.getByName("test") {
    resources.srcDir("src/test/kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

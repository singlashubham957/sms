plugins {
    kotlin("jvm") version "1.7.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("com.jfrog.artifactory") version "4.29.0"
    id("com.github.b3er.local.properties") version "1.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("info.solidsoft.pitest") version "1.7.0"
    application
    jacoco
}

application {
    mainClass.set("sms.Main")
}

repositories {
    mavenCentral()
    google()
    mavenLocal()
}

dependencies {

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("javax.activation:activation:1.1.1")
    implementation("org.glassfish.jersey.core:jersey-server:3.1.3")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.3")
    implementation("org.glassfish.hk2:hk2-metadata-generator:3.0.5")
    implementation("org.glassfish.jersey.containers:jersey-container-netty-http:3.1.3")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.3")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_18.toString()))
    }
}

allprojects {

    group = "money.jupiter.inv.rules"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.artifactory")
    apply(plugin = "java")
    apply(plugin = "info.solidsoft.pitest")

    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_18.toString()
        }
    }

    tasks.getByName<Test>("test") {
//        dependsOn(tasks.pitest)
        useJUnitPlatform()
    }

    pitest {
        setProperty("verbose", true)
        setProperty("failWhenNoMutations", false)
        setProperty("pitestVersion", "1.7.0")
        setProperty("junit5PluginVersion", "0.12")
        setProperty("testPlugin", "junit5")
        setProperty("targetClasses", listOf("money.jupiter.inv.rules.*"))
        setProperty("outputFormats", listOf("HTML", "XML"))
        setProperty("excludedTestClasses", listOf("money.jupiter.inv.rules.http.*"))
        setProperty("exportLineCoverage", true)
        setProperty("reportDir", project.buildDir.resolve("reports/pitest"))
        setProperty("timestampedReports", false)
    }
}

subprojects {
    apply(plugin = "java")

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
}

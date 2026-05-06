import buildsrc.convention.dsl.*

plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring-boot")
}

group = "com.github.cc007.blueart"
version = "0.0.1-SNAPSHOT"
description = "BlueArt server-rendered ATProto art browsing and discovery web application module"

repositories {
    mavenLocal()
}

dependencies {
    implementation {
        +libs.bundles.kotlinxEcosystem
        kotlin("reflect")
        kotlinx("html-jvm", version = "0.12.0")
        +"org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.7.14"
        springBoot("h2console")
        springBoot("starter-actuator")
        springBoot("starter-data-jpa")
        springBoot("starter-liquibase")
        springBoot("starter-restclient")
        springBoot("starter-security")
        springBoot("starter-webmvc")
        springModulith("starter-core")
        springModulith("starter-jpa")
        bsky("core", version = "0.3.0") {
            exclude(group = "work.socialhub.kbsky", module = "core-jvm")
        }
        bsky("core-jvm", version = "1.0.0-SNAPSHOT")
        bsky("auth", version = "0.3.0")
        bsky("stream", version = "0.3.0")
        +"io.github.wimdeblauwe:htmx-spring-boot:5.0.0"
        +"tools.jackson.module:jackson-module-kotlin"
        +"io.github.oshai:kotlin-logging-jvm:5.1.0"
    }

    developmentOnly {
        springBoot("devtools")
    }

    runtimeOnly {
        springModulith("actuator")
        springModulith("observability")
        +"com.h2database:h2"
        +"io.micrometer:micrometer-registry-prometheus"
    }

    annotationProcessor {
        springBoot("configuration-processor")
    }

    testImplementation {
        kotlin("test-junit5")
        springBoot("starter-actuator-test")
        springBoot("starter-data-jpa-test")
        springBoot("starter-liquibase-test")
        springBoot("starter-restclient-test")
        springBoot("starter-security-test")
        springBoot("starter-webmvc-test")
        springBoot("testcontainers")
        springModulith("starter-test")
        +"org.testcontainers:testcontainers-junit-jupiter"
    }

    testRuntimeOnly {
        +"org.junit.platform:junit-platform-launcher"
    }
}
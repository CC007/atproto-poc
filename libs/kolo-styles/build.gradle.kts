import buildsrc.convention.dsl.implementation
import buildsrc.convention.dsl.kotlin
import buildsrc.convention.dsl.spring
import buildsrc.convention.dsl.testImplementation

plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("buildsrc.convention.spring-boot")
}

description = "Reusable styling primitives for co-located Kotlin-first style infrastructure"

dependencies {
    implementation {
        spring("webmvc")
    }

    testImplementation {
        spring("webmvc")
        kotlin("test-junit5")
    }
}


import buildsrc.convention.dsl.*
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("buildsrc.convention.kotlin-jvm")
    kotlin("plugin.spring")
    id("buildsrc.convention.spring-boot") apply false
    id("io.spring.dependency-management")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

description = "Reusable styling primitives for co-located Kotlin-first style infrastructure"

dependencies {
    implementation {
        kotlinx("html-jvm", version = "0.12.0")
        spring("webmvc")
        +"org.slf4j:slf4j-api"
        +"io.github.oshai:kotlin-logging-jvm:5.1.0"
    }

    testImplementation {
        spring("webmvc")
        kotlin("test-junit5")
    }
}


plugins {
    id("buildsrc.convention.kotlin-jvm")
}

description = "Reusable styling primitives for co-located Kotlin-first style infrastructure"

val springBootVersion = libs.versions.springBoot.get()

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("org.springframework:spring-webmvc")

    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    testImplementation("org.springframework:spring-webmvc")
    testImplementation(kotlin("test-junit5"))
}


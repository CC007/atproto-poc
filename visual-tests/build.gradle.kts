import buildsrc.convention.dsl.kotlin
import buildsrc.convention.dsl.testImplementation
import buildsrc.convention.dsl.testRuntimeOnly
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("io.spring.dependency-management")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.modulith:spring-modulith-bom:2.0.1")
    }
}

description = "Playwright visual regression test module for BlueArt"

dependencies {
    testImplementation {
        +project(":app")
        kotlin("test-junit5")
        +"org.junit.jupiter:junit-jupiter-params"
        +"org.springframework.boot:spring-boot-starter-test"
        +"com.microsoft.playwright:playwright:1.54.0"
        +"com.github.romankh3:image-comparison:4.4.0"
    }

    testRuntimeOnly {
        +"org.junit.platform:junit-platform-launcher"
    }
}

tasks.test {
    enabled = false
    description = "Disabled in default test lane; use visualTest"
}

fun Test.configureVisualLane(updateBaselines: Boolean) {
    useJUnitPlatform()
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    maxParallelForks = 1
    systemProperty("blueart.visual.moduleDir", projectDir.absolutePath)
    systemProperty("blueart.visual.updateBaselines", updateBaselines.toString())
}

tasks.register<Test>("visualTest") {
    group = "verification"
    description = "Runs visual regression assertions against committed baselines"
    configureVisualLane(updateBaselines = false)
}

tasks.register<Test>("updateVisualBaselines") {
    group = "verification"
    description = "Refreshes visual baselines after explicit developer approval"
    configureVisualLane(updateBaselines = true)
}

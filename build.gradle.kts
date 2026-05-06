group = "com.github.cc007.blueart"
version = "0.0.1-SNAPSHOT"
description = "BlueArt multi-module Gradle root project"

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
    }
}

tasks.register("bootRun") {
    group = "application"
    description = "Runs the BlueArt application module"
    dependsOn(":app:bootRun")
}
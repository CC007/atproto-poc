package buildsrc.convention.dsl

import org.gradle.kotlin.dsl.DependencyHandlerScope


fun ConfigurationSpecificDependencyHandlerScope.kotlin(module: String, group: String? = null, version: String? = null) {
    id("org.jetbrains.kotlin${group?.let { ".$it" } ?: ""}:kotlin-$module${version?.let { ":$it" } ?: ""}")
}

fun ConfigurationSpecificDependencyHandlerScope.kotlinx(module: String, group: String? = null, version: String? = null) {
    id("org.jetbrains.kotlinx${group?.let { ".$it" } ?: ""}:kotlinx-$module${version?.let { ":$it" } ?: ""}")
}
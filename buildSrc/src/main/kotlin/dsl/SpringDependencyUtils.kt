package buildsrc.convention.dsl

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun ConfigurationSpecificDependencyHandlerScope.spring(module: String, group: String? = null) {
    id("org.springframework${group?.let { ".$it" } ?: ""}:spring-$module")
}

fun ConfigurationSpecificDependencyHandlerScope.springBoot(module: String) {
    spring("boot-$module", group = "boot")
}

fun ConfigurationSpecificDependencyHandlerScope.springModulith(module: String) {
    spring("modulith-$module", group = "modulith")
}

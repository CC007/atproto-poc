package buildsrc.convention.dsl

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.spring(module: String, group: String? = null) {
    id("org.springframework${group?.let { ".$it" } ?: ""}:spring-$module")
}

fun DependencyHandlerScope.springBoot(module: String) {
    spring("boot-$module", group = "boot")
}

fun DependencyHandlerScope.springModulith(module: String) {
    spring("modulith-$module", group = "modulith")
}

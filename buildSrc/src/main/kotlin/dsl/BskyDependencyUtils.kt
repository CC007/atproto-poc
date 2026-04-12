package buildsrc.convention.dsl

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope


fun ConfigurationSpecificDependencyHandlerScope.bsky(
    module: String,
    group: String? = null,
    version: String? = null,
    dependencyConfiguration: ExternalModuleDependency.() -> Unit = {}
) {
    id(
        "work.socialhub.kbsky${group?.let { ".$it" } ?: ""}:$module${version?.let { ":$it" } ?: ""}",
        dependencyConfiguration
    )
}

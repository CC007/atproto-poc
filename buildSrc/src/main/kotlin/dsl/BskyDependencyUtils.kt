package buildsrc.convention.dsl

import org.gradle.kotlin.dsl.DependencyHandlerScope


fun ConfigurationSpecificDependencyHandlerScope.bsky(module: String, group: String? = null, version: String? = null) {
    id("work.socialhub.kbsky${group?.let { ".$it" } ?: ""}:$module${version?.let { ":$it" } ?: ""}")
}

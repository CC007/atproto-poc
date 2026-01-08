package buildsrc.convention.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.GradleDsl


class ConfigurationSpecificDependencyHandlerScope(
    dependencyHandlerScope: DependencyHandlerScope,
    val configurationType: String
) : DependencyHandler by dependencyHandlerScope {

    @GradleDsl
    operator fun String.unaryPlus(): Dependency? {
        return add(configurationType, this)
    }

    @GradleDsl
    fun id(dependencyNotation: Any): Dependency? {
        return add(configurationType, dependencyNotation)
    }
}

private fun DependencyHandlerScope.forConfiguration(
    configurationType: String,
    config: ConfigurationSpecificDependencyHandlerScope.() -> Unit
) {
    with(ConfigurationSpecificDependencyHandlerScope(this, configurationType)) {
        config()
    }
}

@GradleDsl
fun DependencyHandlerScope.implementation(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("implementation", config)

@GradleDsl
fun DependencyHandlerScope.developmentOnly(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("developmentOnly", config)

@GradleDsl
fun DependencyHandlerScope.runtimeOnly(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("runtimeOnly", config)

@GradleDsl
fun DependencyHandlerScope.annotationProcessor(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("annotationProcessor", config)

@GradleDsl
fun DependencyHandlerScope.testImplementation(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("testImplementation", config)

@GradleDsl
fun DependencyHandlerScope.testRuntimeOnly(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("testRuntimeOnly", config)

@GradleDsl
fun DependencyHandlerScope.testAnnotationProcessor(config: ConfigurationSpecificDependencyHandlerScope.() -> Unit) =
    forConfiguration("testAnnotationProcessor", config)

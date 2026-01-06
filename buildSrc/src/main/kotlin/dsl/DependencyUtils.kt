package buildsrc.convention.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.GradleDsl


var dependencyHandlerType: String? = null
@Suppress("UnusedReceiverParameter")
private var DependencyHandlerScope.configurationType: String?
    get() = dependencyHandlerType
    set(value) {
        dependencyHandlerType = value
    }

private fun DependencyHandlerScope.forConfiguration(configurationType: String, config: DependencyHandlerScope.() -> Unit) {
    this.configurationType = configurationType
    config()
    this.configurationType = null
}

@GradleDsl
fun DependencyHandlerScope.implementation(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("implementation", config)

@GradleDsl
fun DependencyHandlerScope.developmentOnly(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("developmentOnly", config)

@GradleDsl
fun DependencyHandlerScope.runtimeOnly(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("runtimeOnly", config)

@GradleDsl
fun DependencyHandlerScope.annotationProcessor(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("annotationProcessor", config)

@GradleDsl
fun DependencyHandlerScope.testImplementation(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("testImplementation", config)

@GradleDsl
fun DependencyHandlerScope.testRuntimeOnly(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("testRuntimeOnly", config)

@GradleDsl
fun DependencyHandlerScope.testAnnotationProcessor(config: DependencyHandlerScope.() -> Unit) =
    forConfiguration("testAnnotationProcessor", config)

@GradleDsl
fun DependencyHandlerScope.id(dependencyNotation: Any): Dependency? {
    val type = configurationType ?: throw IllegalStateException("Configuration type is required")
    return add(type, dependencyNotation)
}
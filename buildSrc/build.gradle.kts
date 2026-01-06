plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(24) // kotlin-dsl still needs kotlin 2.2.20, and only 2.3.0+ supports jdk 25
}

dependencies {
    implementation(libs.kotlinJvmGradlePlugin)
    implementation(libs.kotlinSpringGradlePlugin)
    implementation(libs.kotlinJpaGradlePlugin)
    implementation(libs.springBootPlugin)
    implementation(libs.springDependencyManagementPlugin)
}
plugins {
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle.buildtools)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
        classpath(libs.kotlin.serialization)
        classpath(libs.kotlin.ksp)
        classpath(libs.protobuf.gradlePlugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
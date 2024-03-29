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
        classpath(libs.junit5)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
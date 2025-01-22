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

tasks.register("printModulesWithTests") {
    doLast {
        val modulesWithTests = subprojects.filter { subproject ->
            val androidTestDir = file("${subproject.projectDir}/src/androidTest")
            androidTestDir.exists() && androidTestDir.walkTopDown().any { it.isFile && (it.extension == "kt" || it.extension == "java") }
        }.map { subproject ->
            subproject.path
        }
        file("modules_with_tests.txt").writeText(modulesWithTests.joinToString(" "))
    }
}


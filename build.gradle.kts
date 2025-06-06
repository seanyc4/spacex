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

// Gets a list of all modules that have androidTest sources for CI
tasks.register("printAndroidTestModules") {
    doLast {
        val modulesWithTests = subprojects.filter { subproject ->
            val androidTestDir = file("${subproject.projectDir}/src/androidTest")
            androidTestDir.exists() && androidTestDir.walkTopDown().any { it.isFile && (it.extension == "kt" || it.extension == "java") }
        }.map { subproject ->
            subproject.path
        }
        modulesWithTests.forEach { module ->
            println("Module with androidTest: $module")
        }
    }
}

tasks.register("printAppVersionName") {
    doLast {
        println(Android.versionName)
    }
}

tasks.register("printAppVersionCode") {
    doLast {
        println(Android.versionCode)
    }
}
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.8.2.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.22-1.0.17")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

//        classpath("com.android.tools.build:gradle:${libs.versions.gradlePlugin.get()}")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions..get()}")
//        classpath("de.mannodermaus.gradle.plugins:android-junit5:${libs.versions.junit5Plugin.get()}")
//        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.get()}")
//        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${libs.versions.kotlinKspPlugin.get()}")
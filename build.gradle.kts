
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(Build.gradle)
        classpath(Build.kotlin_gradle_plugin)
        classpath(Build.junit5)
        classpath(Build.hilt_android)
        classpath(Build.kotlin_gradle_plugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
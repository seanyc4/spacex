
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(Build.gradle)
        classpath(Build.kotlin_gradle_plugin)
        classpath(Build.safe_args)
        classpath(Build.junit5)
        classpath(Build.hilt_android)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
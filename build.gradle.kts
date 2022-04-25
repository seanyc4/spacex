
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
        classpath(Build.hilt_android)
        classpath(Build.safe_args)
        classpath(Build.secrets_gradle_plugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
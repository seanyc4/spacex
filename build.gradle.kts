plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.room) apply false
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
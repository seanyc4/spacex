plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

// Gets a list of all modules that have androidTest sources for CI
abstract class PrintAndroidTestModulesTask : DefaultTask() {
    @get:Input
    abstract val modulesWithKt: ListProperty<String>

    @TaskAction
    fun printModules() {
        modulesWithKt.get().forEach { println("Module with androidTest: $it") }
    }
}

val modulesWithKtList = subprojects.filter { subproject ->
    val androidTestDir = subproject.projectDir.resolve("src/androidTest")
    androidTestDir.exists() && androidTestDir.walkTopDown().any { it.isFile && it.extension == "kt" }
}.map { it.path }

tasks.register("printAndroidTestModules", PrintAndroidTestModulesTask::class) {
    modulesWithKt.set(modulesWithKtList)
    group = "verification"
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
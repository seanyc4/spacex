apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = Modules.core_test_namespace
}

dependencies {
    implementation(libs.kotlin.coroutinestest)
    implementation(libs.testArchCore)
    implementation(libs.jupiterApi)
    implementation(libs.compose.uiTestJunit4)
    implementation(libs.testRunner)
    implementation(libs.hilt.androidtesting)
}
apply {
    from("$rootDir/android-base.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
}

android {
    namespace = Modules.core_database_api_namespace
}

dependencies {
    implementation(projects.core)
    implementation(projects.launch.api)
    implementation(libs.room.ktx)
}
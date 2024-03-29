apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = Modules.core_datastore_test_namespace
}

dependencies {
    implementation(projects.coreDatastore)
    implementation(libs.dataStore)
}
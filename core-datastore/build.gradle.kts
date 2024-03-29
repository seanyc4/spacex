apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = Modules.core_datastore_namespace
}

dependencies {
    testImplementation(projects.coreDatastoreTest)
    implementation(libs.dataStore)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.core.datastore.implementation"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore.api)
    implementation(libs.dataStore.preferences)

    testImplementation(libs.bundles.unitTestBundle)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.datastore.implementation"
}

dependencies {
    implementation(projects.datastore.api)
    implementation(libs.dataStore)

    testImplementation(projects.datastore.test)
    testImplementation(libs.bundles.unitTestBundle)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
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
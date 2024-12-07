apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.core.datastore.implementation"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore.api)
    api(projects.core.datastoreProto)

    implementation(libs.dataStore)
    implementation(libs.dataStore.preferences)

    testImplementation(libs.bundles.unitTestBundle)
}
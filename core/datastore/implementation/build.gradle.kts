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
    implementation(projects.feature.launch.api)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastoreProto)

    implementation(libs.dataStore)
    implementation(libs.dataStore.preferences)
    implementation(libs.protobuf.kotlin.lite)

    testImplementation(libs.bundles.unitTestBundle)
}
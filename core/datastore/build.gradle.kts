apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.datastore.implementation"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    implementation(projects.launch.api)
    implementation(projects.core.common)
    implementation(projects.core.domain)

    api(projects.core.datastoreProto)
    api(libs.dataStore)
    api(libs.dataStore.preferences)
    api(libs.protobuf.kotlin.lite)

    testImplementation(libs.bundles.unitTestBundle)
}
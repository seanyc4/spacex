apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
    id(libs.plugins.junit5.get().pluginId)
    kotlin(libs.plugins.android.get().pluginId)
}

android {
    namespace = "com.seancoyle.launch.implementation"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.test)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.datastore.api)
    implementation(projects.database)
    implementation(projects.launch.api)

    implementation(libs.appCompat)
    implementation(libs.coreKtx)
    implementation(libs.glideCompose)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.immutable)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.gson)

    androidTestImplementation(libs.bundles.androidTestBundle)
    testImplementation(libs.bundles.unitTestBundle)
}
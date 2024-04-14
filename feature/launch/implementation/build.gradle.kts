apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
    kotlin(libs.plugins.android.get().pluginId)
}

android {
    namespace = "com.seancoyle.feature.launch.implementation"

    packaging {
        resources.pickFirsts.add("META-INF/LICENSE.md")
        resources.pickFirsts.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.database)
    api(projects.feature.launch.api)

    implementation(libs.appCompat)
    implementation(libs.coreKtx)
    implementation(libs.glideCompose)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.immutable)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.gson)

    androidTestImplementation(projects.core.datastore)
    androidTestImplementation(libs.bundles.androidTestBundle)
    androidTestImplementation(projects.core.test)
    testImplementation(projects.core.test)
    testImplementation(libs.bundles.unitTestBundle)
}
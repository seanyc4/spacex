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
    namespace = Modules.launch_impl_namespace
}

dependencies {

    implementation(projects.core)
    implementation(projects.coreTest)
    implementation(projects.coreUi)
    implementation(projects.coreDatastore)
    implementation(projects.database)
    implementation(projects.launch.api)

    implementation(libs.appCompat)
    implementation(libs.coreKtx)
    implementation(libs.glideCompose)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.immutable)
    implementation(libs.materialDialogsCore)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.square.loggingInterceptor)
    implementation(libs.square.okhttp)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.gson)
    implementation(libs.square.mockwebserver)

    androidTestImplementation(libs.bundles.androidTestBundle)
    testImplementation(libs.bundles.unitTestBundle)
}
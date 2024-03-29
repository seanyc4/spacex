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
    implementation(projects.coreDatabase.api)
    implementation(projects.coreDatabase.implementation)
    implementation(projects.launch.api)

    implementation(libs.appcompat)
    implementation(libs.coreKtx)
    implementation(libs.glideCompose) {
        exclude(group = "androidx.test", module = "core-ktx")
    }
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.immutable)
    implementation(libs.materialDialogsCore)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.square.logginginterceptor)
    implementation(libs.square.okhttp)
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.gson)
    implementation(libs.square.mockwebserver)
    implementation(libs.bundles.androidTestBundle)

}
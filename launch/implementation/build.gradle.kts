apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.junit5)
    id(Plugins.android_library)
    id(Plugins.ksp)
    id(Plugins.kotlin)
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

    testImplementation(libs.testArchCore)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiterEngine)
    testImplementation(libs.jupiterApi)
    testImplementation(libs.jupiterParams)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlin.coroutinestest)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testCoreKtx)
    androidTestImplementation(libs.kotlin.junit)

}
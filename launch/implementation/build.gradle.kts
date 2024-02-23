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
    implementation(projects.coreTesting)
    implementation(projects.coreUi)
    implementation(projects.coreDatastore)
    implementation(projects.coreDatabase.api)
    implementation(projects.coreDatabase.implementation)
    implementation(projects.launch.api)

    implementation(AndroidX.app_compat)
    implementation(AndroidX.core_ktx)
    implementation(Glide.glide_compose) {
        exclude(group = "androidx.test", module = "core-ktx")
    }
    //ksp(Glide.glide_compiler)
    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
    implementation(Kotlin.kotlin_immutable)
    implementation(Room.room_ktx)
    implementation(Room.room_runtime)
    ksp(Room.room_compiler)
    implementation(ScalingPixels.scaling_pixels)
    implementation(Square.interceptor)
    implementation(Square.ok_http)
    implementation(Square.retrofit)
    implementation(Square.retrofit_gson)
    implementation(Square.mock_web_server)

    testImplementation(AndroidTestDependencies.test_arch_core)
    testImplementation(AndroidTestDependencies.mockk)
    testImplementation(AndroidTestDependencies.jupiter_engine)
    testImplementation(AndroidTestDependencies.jupiter_api)
    testImplementation(AndroidTestDependencies.jupiter_params)
    testImplementation(AndroidTestDependencies.junit4)
    testImplementation(KotlinTest.coroutines_test)

    androidTestImplementation(AndroidTestDependencies.test_runner)
    androidTestImplementation(AndroidTestDependencies.test_core_ktx)
    androidTestImplementation(KotlinTest.kotlin_test)

}
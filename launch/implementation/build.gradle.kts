apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("de.mannodermaus.android-junit5")
}

dependencies {

    "implementation"(project(Modules.core))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.core_datastore))
    "implementation"(project(Modules.core_database))
    "implementation"(project(Modules.launchApi))
    "implementation"(AndroidX.app_compat)
    "implementation"(AndroidX.core_ktx)
    "implementation"(AndroidX.data_store)
    "implementation"(AndroidX.lifecycle_compose_viewmodel)
    "implementation"(Glide.glide)
    "implementation"(Glide.glide_compose)
    "kapt"(Glide.glide_compiler)
    "implementation"(Kotlin.coroutines_core)
    "implementation"(Kotlin.coroutines_android)
    "implementation"(Google.swipe_refresh_layout)
    "implementation"(Room.room_ktx)
    "implementation"(Room.room_runtime)
    "kapt"(Room.room_compiler)
    "implementation"(ScalingPixels.scaling_pixels)
    "implementation"(Square.interceptor)
    "implementation"(Square.ok_http)
    "implementation"(Square.retrofit)
    "implementation"(Square.retrofit_gson)
    "implementation"(Square.mock_web_server)

    "testImplementation"(project(Modules.core_datastore_test))
    "testImplementation"(AndroidTestDependencies.test_arch_core)
    "testImplementation"(AndroidTestDependencies.coroutines_test)
    "testImplementation"(TestDependencies.mockk)
    "testImplementation"(TestDependencies.jupiter_engine)
    "testImplementation"(TestDependencies.jupiter_api)
    "testImplementation"(TestDependencies.jupiter_params)
    "testImplementation"(TestDependencies.junit4)
}
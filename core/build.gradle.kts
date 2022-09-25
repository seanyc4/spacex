apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins{
    id("de.mannodermaus.android-junit5")
}

dependencies {
    "implementation"(Kotlin.coroutines_android)
    "implementation"(AndroidX.lifecycle_live_data_ktx)
    "implementation"(Square.retrofit)
    "implementation"(Timber.timber)
    "implementation"(ScalingPixels.scaling_pixels)
    "implementation"(Google.material)
    "implementation"(Google.material)

    "implementation"(AndroidTestDependencies.coroutines_test)
    "implementation"(AndroidTestDependencies.mockk)
    "implementation"(AndroidTestDependencies.test_arch_core)

    "implementation"(TestDependencies.jupiter_engine)
    "implementation"(TestDependencies.jupiter_api)
    "implementation"(TestDependencies.jupiter_params)
    "implementation"(TestDependencies.junit4)
}
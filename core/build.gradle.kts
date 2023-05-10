apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.core_namespace
}

dependencies {
    "implementation"(Kotlin.coroutines_android)
    "implementation"(Square.retrofit)
    "implementation"(ScalingPixels.scaling_pixels)
    "implementation"(AndroidTestDependencies.coroutines_test)
    "implementation"(AndroidTestDependencies.test_arch_core)
    "implementation"(TestDependencies.jupiter_api)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.core_namespace
}

dependencies {
    implementation(Kotlin.coroutines_android)
    implementation(KotlinTest.coroutines_test)
    implementation(Square.retrofit)
    implementation(AndroidTestDependencies.test_arch_core)
    implementation(AndroidTestDependencies.jupiter_api)
}
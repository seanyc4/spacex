plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    api(HiltTest.hilt_android_testing)

    testImplementation(TestDependencies.junit4)
    testImplementation(TestDependencies.jupiter_api)
    testImplementation(TestDependencies.jupiter_params)
    testImplementation(TestDependencies.mockk)
    testRuntimeOnly(TestDependencies.jupiter_engine)

    api(AndroidTestDependencies.androidx_test_ext)
    api(AndroidTestDependencies.coroutines_test)
    api(AndroidTestDependencies.espresso_contrib)
    api(AndroidTestDependencies.espresso_core)
    api(AndroidTestDependencies.espresso_intents)
    api(AndroidTestDependencies.idling_resource)
    api(AndroidTestDependencies.kotlin_test)
    api(AndroidTestDependencies.mockk_android)
    androidTestUtil(AndroidTestDependencies.test_orchestrator)
    api(AndroidTestDependencies.test_rules)
    api(AndroidTestDependencies.test_runner)
    api(AndroidTestDependencies.test_core_ktx)
}
plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
    id("de.mannodermaus.android-junit5")
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
    implementation(project(Modules.core))
    implementation(project(Modules.launchConstants))
    implementation(project(Modules.launchDataSource))
    implementation(project(Modules.launchDomain))
    implementation(project(Modules.launchViewState))
    testImplementation(project(Modules.launchDataSourceTest))

    api(TestDependencies.jupiter_engine)
    api(TestDependencies.jupiter_api)
    api(TestDependencies.jupiter_params)
    api(TestDependencies.junit4)

    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
    implementation(Square.mock_web_server)
}
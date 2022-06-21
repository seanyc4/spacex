plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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
    implementation(project(Modules.launchModels))
    implementation(project(Modules.launchDataSource))
    implementation(project(Modules.launchViewState))

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
}
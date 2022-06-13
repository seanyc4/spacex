
plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
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
        // Enable Java 8 time below api 26
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.constants))

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Timber.timber)

    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
}
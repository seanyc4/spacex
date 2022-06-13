
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

    implementation(AndroidX.data_store)
    coreLibraryDesugaring(AndroidX.desurgar)
    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Timber.timber)

}
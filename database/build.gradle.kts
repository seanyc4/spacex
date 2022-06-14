
plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
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

    implementation(Room.room_ktx)
    implementation(Room.room_runtime)
    kapt(Room.room_compiler)
    coreLibraryDesugaring(AndroidX.desurgar)


}
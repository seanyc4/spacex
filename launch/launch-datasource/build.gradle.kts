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
    implementation(project(Modules.constants))
    implementation(project(Modules.core))
    implementation(project(Modules.dataAndroid))
    implementation(project(Modules.launchDomain))

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)

    implementation(Room.room_ktx)
    implementation(Room.room_runtime)
    kapt(Room.room_compiler)

    implementation(Square.interceptor)
    implementation(Square.ok_http)
    implementation(Square.retrofit)
    implementation(Square.retrofit_gson)
}
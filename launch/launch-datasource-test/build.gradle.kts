plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    implementation(project(Modules.coreTesting))
    implementation(project(Modules.launchConstants))
    implementation(project(Modules.launchDataSource))
    implementation(project(Modules.launchDomain))
    implementation(Square.retrofit_gson)
    implementation(Square.mock_web_server)

}
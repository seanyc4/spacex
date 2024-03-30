apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.core_ui"
}

dependencies {
    implementation(projects.core)
    implementation(libs.lifecycle.viewmodelKtx)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.scalingPixels)
}
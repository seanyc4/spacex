apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.test)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.youtubePlayer)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.seancoyle.navigation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.compose.material3.adaptive.navigation3)
    implementation(libs.compose.material3.adaptive.layout)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.kotlinx.serialization.core)
}

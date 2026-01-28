apply(from = "$rootDir/android-base.gradle")
apply(from = "$rootDir/hilt.gradle")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.hiltuitest"
}
apply(from = "$rootDir/android-base.gradle")
apply(from = "$rootDir/hilt.gradle")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.common"

    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    androidTestImplementation(projects.core.test)
    androidTestImplementation(libs.bundles.unitTestBundle)
}
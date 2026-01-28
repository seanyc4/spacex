apply(from = "$rootDir/android-base.gradle")
apply(from = "$rootDir/android-base-compose.gradle")
apply(from = "$rootDir/hilt.gradle")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seancoyle.core.test"

    packaging {
        resources.pickFirsts.add("META-INF/LICENSE.md")
        resources.pickFirsts.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(libs.bundles.androidTestBundle)
}
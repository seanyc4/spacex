apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.core.test"
}

dependencies {
    implementation(libs.bundles.androidTestBundle)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.square.retrofit.core)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.feature.launch.api"
}

dependencies {
    api(projects.core.common)
    api(projects.core.domain)
    implementation(libs.googleGson)
}
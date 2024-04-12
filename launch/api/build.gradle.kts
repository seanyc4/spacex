apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.launch.api"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.googleGson)
}
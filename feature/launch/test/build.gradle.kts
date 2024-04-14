apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
}

android {
    namespace = "com.seancoyle.feature.launch.test"
}

dependencies {
    api(projects.feature.launch.api)
}
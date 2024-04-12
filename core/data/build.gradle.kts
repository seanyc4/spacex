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
    implementation(projects.core.domain)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.square.retrofit.core)
}
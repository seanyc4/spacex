apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.launch_test_namespace
}

dependencies {
    implementation(projects.launch.api)
    implementation(Google.google_gson)
}
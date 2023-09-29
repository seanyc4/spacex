apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.launch_api_namespace
}

dependencies {
    implementation(projects.core)
}
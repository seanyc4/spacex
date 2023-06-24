apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.launch_api_namespace
}

dependencies {
    "implementation"(project(Modules.core))
}
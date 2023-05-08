apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.seancoyle.launch.api"
}

dependencies {
    "api"(project(Modules.core))
}
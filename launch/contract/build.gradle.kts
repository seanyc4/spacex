apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.launch_contract_namespace
}

dependencies {
    "api"(project(Modules.core))
}
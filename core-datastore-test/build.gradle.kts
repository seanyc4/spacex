apply {
    from("$rootDir/android-base.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.seancoyle.core_datastore_test"
}

dependencies {
    "implementation"(project(Modules.core_datastore))
}
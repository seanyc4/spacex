apply {
    from("$rootDir/android-base.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.core_datastore_test_namespace
}

dependencies {
    "implementation"(project(Modules.core_datastore))
}
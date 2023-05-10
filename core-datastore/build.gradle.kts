apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.core_datastore_namespace
}

dependencies {
    "implementation"(AndroidX.data_store)
}
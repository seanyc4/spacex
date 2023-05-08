apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = "com.seancoyle.core_datastore"
}

dependencies {
    "implementation"(AndroidX.data_store)
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Modules.core_datastore_test_namespace
}

dependencies {
    implementation(projects.coreDatastore)
    implementation(AndroidX.data_store)
}
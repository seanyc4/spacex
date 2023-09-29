apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.core_datastore_test_namespace
}

dependencies {
    implementation(projects.coreDatastore)
    implementation(AndroidX.data_store)
}
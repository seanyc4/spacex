apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
    kotlin(Plugins.android)
}

android {
    namespace = Modules.core_datastore_namespace
}

dependencies {
    testImplementation(projects.coreDatastoreTest)
    implementation(AndroidX.data_store)
}
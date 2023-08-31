apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Modules.core_datastore_namespace
}

dependencies {
    testImplementation(projects.coreDatastoreTest)
    implementation(AndroidX.data_store)
}
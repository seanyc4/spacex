apply {
    from("$rootDir/android-base.gradle")
}

plugins {
    id(Plugins.android_library)
    id(Plugins.ksp)
}

android {
    namespace = Modules.core_database_api_namespace
}

dependencies {
    implementation(projects.core)
    implementation(projects.launch.api)
    implementation(Room.room_ktx)
}
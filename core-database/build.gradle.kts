apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id(Plugins.android_library)
    id(Plugins.ksp)
}

android {

    namespace = Modules.core_database_namespace

    defaultConfig {

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }
}

dependencies {
    implementation(projects.core)
    implementation(Room.room_ktx)
    implementation(Room.room_runtime)
    ksp(Room.room_compiler)
}
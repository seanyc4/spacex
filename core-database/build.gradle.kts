apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("com.android.library")
    id("com.google.devtools.ksp") version Kotlin.ksp_version
}

android {
    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    "implementation"(project(Modules.launchConstants))
    "implementation"(Kotlin.ksp)
    "implementation"(Room.room_ktx)
    "implementation"(Room.room_runtime)
    "kapt"(Room.room_compiler)
}
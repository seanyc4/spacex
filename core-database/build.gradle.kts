apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {
    "implementation"(project(Modules.launchConstants))
    "implementation"(Hilt.android)
    "kapt"(Hilt.compiler)
    "androidTestImplementation"(HiltTest.hilt_android_testing)
    "implementation"(Room.room_ktx)
    "implementation"(Room.room_runtime)
    "kapt"(Room.room_compiler)
}
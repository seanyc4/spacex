apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreTesting))
    "api"(project(Modules.coreDatabase))
    "api"(project(Modules.launchDomain))

    "implementation"(Kotlin.coroutines_core)
    "implementation"(Kotlin.coroutines_android)

    "implementation"(Room.room_ktx)
    "implementation"(Room.room_runtime)
    "kapt"(Room.room_compiler)

    "implementation"(Square.interceptor)
    "implementation"(Square.ok_http)
    "implementation"(Square.retrofit)
    "implementation"(Square.retrofit_gson)
    "implementation"(Square.mock_web_server)
}
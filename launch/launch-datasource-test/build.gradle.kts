apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {

    "implementation"(project(Modules.core))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.launchDataSource))
    "implementation"(project(Modules.launchDomain))
    "implementation"(Square.retrofit_gson)
    "implementation"(Square.mock_web_server)

}
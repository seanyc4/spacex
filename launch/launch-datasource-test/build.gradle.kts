apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}
plugins{
    id("com.android.library")
}

android{
    sourceSets {
        getByName("main").resources.srcDir("src/main/res")
    }
}

dependencies {

    "implementation"(project(Modules.core))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.launchDataSource))
    "implementation"(project(Modules.launchDomain))
    "implementation"(Square.retrofit_gson)
    "implementation"(Square.mock_web_server)

}
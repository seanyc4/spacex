apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.movieDataSource))
    "implementation"(project(Modules.movieDomain))

    "implementation"(Kotlinx.coroutines_core) // need for flows

    "testImplementation"(project(Modules.movieDataSourceTest))
    "testImplementation"(Junit.junit4)
    "testImplementation"(Retrofit.mock_web_server)
    "testImplementation"(Retrofit.ok_http)
}
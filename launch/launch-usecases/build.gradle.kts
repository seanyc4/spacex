
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("de.mannodermaus.android-junit5")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.launchDataSource))
    "implementation"(project(Modules.launchDomain))
    "implementation"(project(Modules.launchViewState))

    "testImplementation"(project(Modules.coreDatastoreTest))
    "testImplementation"(project(Modules.launchDataSourceTest))
    "testImplementation"(AndroidTestDependencies.test_arch_core)
    "testImplementation"(AndroidTestDependencies.coroutines_test)

    "testImplementation"(TestDependencies.jupiter_engine)
    "testImplementation"(TestDependencies.jupiter_api)
    "testImplementation"(TestDependencies.jupiter_params)
    "testImplementation"(TestDependencies.junit4)

    "implementation"(Kotlin.coroutines_core)
    "implementation"(Kotlin.coroutines_android)
    "implementation"(Square.mock_web_server)
}
apply {
    from("$rootDir/android-base.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.launchDomain))
}
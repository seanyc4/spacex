apply {
    from("$rootDir/android-base.gradle")
}

dependencies {

    "implementation"(project(Modules.coreDatastore))

}
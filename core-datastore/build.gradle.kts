
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {

    "implementation"(AndroidX.data_store)

}
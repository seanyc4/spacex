apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {
    "implementation"(Kotlin.coroutines_android)
    "implementation"(AndroidX.lifecycle_live_data_ktx)
    "implementation"(Square.retrofit)
    "implementation"(Timber.timber)
}
object Kotlin {
    const val kotlin_version = "1.7.20"

    private const val kotlinx_datetime_version = "0.3.2"
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime_version"

    const val coroutines_core_version = "1.6.4"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_core_version"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_core_version"

    // Used for setting room exported schema in database gradle
    const val ksp_version = "1.7.10-1.0.6"
    const val ksp = "com.google.devtools.ksp:symbol-processing-api:$ksp_version"

    // Need for tests. Plugin doesn't work.
    private const val serialization_version = "1.4.1"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version"
}
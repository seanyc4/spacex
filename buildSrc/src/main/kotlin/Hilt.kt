object Hilt {
    const val hilt_version = "2.50"
    const val android = "com.google.dagger:hilt-android:$hilt_version"
    const val compiler = "com.google.dagger:hilt-compiler:$hilt_version"

    private const val android_hilt_version = "1.2.0"
    const val hilt_viewmodel = "androidx.hilt.hilt-lifecycle-viewmodel:$android_hilt_version"

    private const val hilt_navigation_compose_version = "1.1.0-beta01"
    const val hilt_navigation_compose ="androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version"
}

object HiltTest {
    const val hilt_android_testing = "com.google.dagger:hilt-android-testing:${Hilt.hilt_version}"
}
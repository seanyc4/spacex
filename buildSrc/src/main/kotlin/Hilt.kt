object Hilt {
    const val hilt_version = "2.45"
    const val android = "com.google.dagger:hilt-android:$hilt_version"
    const val compiler = "com.google.dagger:hilt-compiler:$hilt_version"

    private const val hilt_viewmodel_version = "1.0.0-alpha03"
    const val hilt_viewmodel = "androidx.hilt.hilt-lifecycle-viewmodel:$hilt_viewmodel_version"
}

object HiltTest {
    const val hilt_android_testing = "com.google.dagger:hilt-android-testing:${Hilt.hilt_version}"
}
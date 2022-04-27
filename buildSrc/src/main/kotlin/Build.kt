object Build {

    private const val gradle_version = "7.1.3"
    const val gradle = "com.android.tools.build:gradle:$gradle_version"

    private const val secrets_gradle_plugin_verison = "2.0.1"
    const val secrets_gradle_plugin = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:$secrets_gradle_plugin_verison"

    private const val junit5_version = "1.8.2.0"
    const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:$junit5_version"

    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    const val hilt_android = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.hilt_version}"
    const val safe_args = "androidx.navigation:navigation-safe-args-gradle-plugin:${AndroidX.navigation_version}"

}
object Build {

    private const val gradle_version = "8.2.1"
    const val gradle = "com.android.tools.build:gradle:$gradle_version"

    private const val junit5_version = "1.8.2.0"
    const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:$junit5_version"

    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.kotlin_version}"
    const val hilt_android = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.hilt_version}"

    const val kotlin_ksp = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Kotlin.kotlin_ksp_version}"
}
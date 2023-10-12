import Kotlin.coroutines_core_version
import Kotlin.kotlin_version

object Kotlin {
    const val kotlin_version = "1.9.10"
    const val kotlin_ksp_version = "1.9.10-1.0.13" //https://github.com/google/ksp/releases - to be updated inline with kotlin version
    private const val kotlin_immutable_version = "0.3.6"
    const val kotlin_immutable ="org.jetbrains.kotlinx:kotlinx-collections-immutable:${kotlin_immutable_version}"

    private const val kotlinx_datetime_version = "0.4.0"
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime_version"

    const val coroutines_core_version = "1.7.0"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_core_version"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_core_version"

    private const val serialization_version = "1.5.0"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version"
}

object KotlinTest {
    const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_core_version"
    const val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
}

import AndroidX.fragment_version
import AndroidX.navigation_version

object AndroidX {

    private const val app_compat_version = "1.4.1"
    const val app_compat = "androidx.appcompat:appcompat:$app_compat_version"

    private const val core_ktx_version = "1.7.0"
    const val core_ktx = "androidx.core:core-ktx:$core_ktx_version"

    private const val data_store_version = "1.0.0"
    const val data_store = "androidx.datastore:datastore-preferences:$data_store_version"

    private const val desurgar_version = "1.1.5"
    const val desurgar = "com.android.tools:desugar_jdk_libs:$desurgar_version"

    const val fragment_version = "1.4.1"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:$fragment_version"

    const val navigation_version = "2.4.2"
    const val navigation_dynamic =
        "androidx.navigation:navigation-dynamic-features-fragment:$navigation_version"
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:$navigation_version"

    private const val lifecycle_version = "2.4.1"
    const val lifecycle_vm_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    const val lifecycle_live_data_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    const val lifecycle_saved_state = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${lifecycle_version}"

    private const val splash_version = "1.0.0-beta02"
    const val splash_screen = "androidx.core:core-splashscreen:$splash_version"

}

object AndroidXTest {
    private const val version = "1.3.0"
    const val runner = "androidx.test:runner:$version"
    const val navigation_testing = "androidx.navigation:navigation-testing:$navigation_version"
    const val fragment_testing = "androidx.fragment:fragment-testing:$fragment_version"


}
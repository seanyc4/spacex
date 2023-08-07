import Compose.compose_version

object Compose {

    const val compose_compiler_version = "1.5.1"
    const val compose_version = "1.6.0-alpha02"

    const val compose_runtime = "androidx.compose.runtime:runtime:$compose_version"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_livedata = "androidx.compose.runtime:runtime-livedata:$compose_version"
    const val compose_preview = "androidx.compose.ui:ui-tooling-preview:$compose_version"
    const val compose_tooling = "androidx.compose.ui:ui-tooling:$compose_version"
    const val compose_activity = "androidx.activity:activity-compose:$compose_version"
    const val compose_viewbinding = "androidx.compose.ui:ui-viewbinding:$compose_version"

    private const val compose_navigation_version = "2.7.0-rc01"
    const val compose_navigation = "androidx.navigation:navigation-compose:$compose_navigation_version"

    private const val compose_material_version = "1.6.0-alpha02"
    const val compose_foundation = "androidx.compose.foundation:foundation:$compose_material_version"
    const val compose_layout = "androidx.compose.foundation:foundation-layout:$compose_material_version"
    const val compose_material = "androidx.compose.material:material:$compose_material_version"
    const val compose_icons = "androidx.compose.material.material-icons-core:$compose_material_version"
    const val compose_icons_extended = "androidx.compose.material.material-icons-extended:$compose_material_version"
    const val compose_ripple = "androidx.compose.material.material-ripple:$compose_material_version"

    private const val compose_theme_adapter_version = "1.2.1"
    const val compose_theme_adapter = "com.google.android.material:compose-theme-adapter:$compose_theme_adapter_version"

    private const val compose_constraint_layout_version = "1.1.0-alpha11"
    const val compose_constraint_layout = "androidx.constraintlayout:constraintlayout-compose:$compose_constraint_layout_version"
}

object ComposeTest {
    const val compuse_ui_test ="androidx.compose.ui:ui-test:$compose_version"
    const val compuse_ui_test_junit4 ="androidx.compose.ui:ui-test-junit4:$compose_version"
    const val compuse_ui_test_manifest ="androidx.compose.ui:ui-test-manifest:$compose_version"
}
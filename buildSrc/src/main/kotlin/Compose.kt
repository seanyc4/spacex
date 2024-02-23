object Compose {

    const val compose_compiler_version = "1.5.10"
    const val compose_bom_version = "2024.02.01"


    const val compose_bom = "androidx.compose:compose-bom:$compose_bom_version"

    const val compose_runtime = "androidx.compose.runtime:runtime"
    const val compose_ui = "androidx.compose.ui:ui"
    const val compose_preview = "androidx.compose.ui:ui-tooling-preview"
    const val compose_tooling = "androidx.compose.ui:ui-tooling"
    const val compose_activity = "androidx.activity:activity-compose"
    const val compose_viewbinding = "androidx.compose.ui:ui-viewbinding"

    private const val fragment_compose_version = "1.7.0-alpha10"
    const val compose_fragment = "androidx.fragment:fragment-compose:$fragment_compose_version"

    private const val compose_lifecycle_version ="2.7.0-alpha02"
    const val compose_lifecycle ="androidx.lifecycle:lifecycle-runtime-compose:$compose_lifecycle_version"
    const val compose_viewmodel_lifecycle ="androidx.lifecycle:lifecycle-viewmodel-compose:$compose_lifecycle_version"

    private const val compose_navigation_version = "2.7.4"
    const val compose_navigation = "androidx.navigation:navigation-compose:$compose_navigation_version"

    private const val compose_material_version = "1.6.2"
    const val compose_foundation = "androidx.compose.foundation:foundation:$compose_material_version"
    const val compose_layout = "androidx.compose.foundation:foundation-layout:$compose_material_version"
    const val compose_material = "androidx.compose.material:material:$compose_material_version"
    const val compose_icons = "androidx.compose.material.material-icons-core:$compose_material_version"
    const val compose_icons_extended = "androidx.compose.material.material-icons-extended:$compose_material_version"
    const val compose_ripple = "androidx.compose.material.material-ripple:$compose_material_version"

    private const val compose_material3_version = "1.1.2"
    const val compose_material3 = "androidx.compose.material3:material3:$compose_material3_version"

    private const val compose_theme_adapter_version = "1.2.1"
    const val compose_theme_adapter = "com.google.android.material:compose-theme-adapter:$compose_theme_adapter_version"

    private const val compose_constraint_layout_version = "1.1.0-alpha13"
    const val compose_constraint_layout = "androidx.constraintlayout:constraintlayout-compose:$compose_constraint_layout_version"
}

object ComposeTest {
    const val compuse_ui_test ="androidx.compose.ui:ui-test"
    const val compuse_ui_test_junit4 ="androidx.compose.ui:ui-test-junit4"
    const val compuse_ui_test_manifest ="androidx.compose.ui:ui-test-manifest"
}
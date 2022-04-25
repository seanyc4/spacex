
object Compose {
    private const val activity_compose_version = "1.6.0-alpha01"
    const val activity = "androidx.activity:activity-compose:$activity_compose_version"

    const val compose_version = "1.2.0-alpha08"
    const val ui = "androidx.compose.ui:ui:$compose_version"
    const val material = "androidx.compose.material:material:$compose_version"
    const val tooling = "androidx.compose.ui:ui-tooling:$compose_version"
    const val icons = "androidx.compose.material.material-icons-core:$compose_version"
    const val icons_extended = "androidx.compose.material.material-icons-extended:$compose_version"
    const val ripple = "androidx.compose.material.material-ripple:$compose_version"

    private const val compose_constraint_version = "1.0.0"
    const val compose_constraint ="androidx.constraintlayout.constraintlayout-compose: $compose_constraint_version"

    private const val navigation_version = "2.5.0-beta01"
    const val navigation = "androidx.navigation:navigation-compose:$navigation_version"

    private const val hilt_navigation_compose_version = "1.0.0"
    const val hilt_navigation = "androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version"
}

object ComposeTest {
    const val ui_test_junit4 = "androidx.compose.ui:ui-test-junit4:${Compose.compose_version}"
    const val ui_test_manifest = "androidx.compose.ui:ui-test-manifest:${Compose.compose_version}"
}
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.core_ui_namespace
}

dependencies {
    implementation(projects.core)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.scalingpixels)
}
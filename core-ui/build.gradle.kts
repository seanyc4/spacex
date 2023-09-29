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
    "implementation"(projects.core)
    "implementation"(AndroidX.lifecycle_vm_ktx)
    "implementation"(Kotlin.coroutines_android)
    "implementation"(ScalingPixels.scaling_pixels)
}
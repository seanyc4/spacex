apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id("com.android.library")
}

android {
    namespace = Modules.launch_impl_namespace
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(AndroidX.lifecycle_vm_ktx)
    "implementation"(Kotlin.coroutines_android)
    "implementation"(ScalingPixels.scaling_pixels)
}
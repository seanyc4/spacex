
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("de.mannodermaus.android-junit5")
}

dependencies {

    "coreLibraryDesugaring"(Java8Time.java8Time)
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.core_datastore))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.launchDomain))
    "implementation"(project(Modules.launchUseCases))
    "implementation"(project(Modules.launchViewState))

    "implementation"(AndroidX.app_compat)
    "implementation"(AndroidX.core_ktx)
    "implementation"(AndroidX.data_store)

    "implementation"(Glide.glide)
    "kapt"(Glide.glide_compiler)

    "implementation"(Google.swipe_refresh_layout)

    "implementation"(ScalingPixels.scaling_pixels)

    "testImplementation"(project(Modules.core_datastore_test))
    "testImplementation"(project(Modules.launchDataSourceTest))
}

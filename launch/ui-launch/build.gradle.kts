
apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    id("de.mannodermaus.android-junit5")
}

dependencies {

    "coreLibraryDesugaring"(Java8Time.java8Time)
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreDatastore))
    "implementation"(project(Modules.launchConstants))
    "implementation"(project(Modules.launchDomain))
    "implementation"(project(Modules.launchUseCases))
    "implementation"(project(Modules.launchViewState))

    "implementation"(AndroidX.app_compat)
    "implementation"(AndroidX.core_ktx)
    "implementation"(AndroidX.data_store)
    "implementation"(AndroidX.fragment_ktx)
    "implementation"(AndroidX.lifecycle_live_data_ktx)
    "implementation"(AndroidX.lifecycle_vm_ktx)
    "implementation"(AndroidX.lifecycle_saved_state)
    "implementation"(AndroidX.navigation_dynamic)
    "implementation"(AndroidX.navigation_fragment)
    "implementation"(AndroidX.navigation_ui)
    "kapt"(AndroidX.lifecycle_compiler)

    "implementation"(Glide.glide)
    "kapt"(Glide.glide_compiler)

    "implementation"(Google.constraint_layout)
    "implementation"(Google.card_view)
    "implementation"(Google.material)
    "implementation"(Google.recycler_view)
    "implementation"(Google.swipe_refresh_layout)

    "implementation"(MaterialDialogs.material_dialogs)

    "implementation"(ScalingPixels.scaling_pixels)
    "implementation"(Timber.timber)


    "testImplementation"(project(Modules.coreDatastoreTest))
    "testImplementation"(project(Modules.launchDataSourceTest))
    "testImplementation"(AndroidTestDependencies.coroutines_test)
    "testImplementation"(AndroidTestDependencies.mockk)
    "testImplementation"(AndroidTestDependencies.test_arch_core)

    "testImplementation"(TestDependencies.jupiter_engine)
    "testImplementation"(TestDependencies.jupiter_api)
    "testImplementation"(TestDependencies.jupiter_params)
    "testImplementation"(TestDependencies.junit4)
}

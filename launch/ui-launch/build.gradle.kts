apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/hilt.gradle")
}

dependencies {

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
    "debugImplementation"(AndroidXTest.fragment_testing)
    "androidTestImplementation"(AndroidXTest.navigation_testing)

    "implementation"(Glide.glide)
    "kapt"(Glide.glide_compiler)

    "implementation"(Google.constraint_layout)
    "implementation"(Google.card_view)
    "implementation"(Google.material)
    "implementation"(Google.recycler_view)
    "implementation"(Google.swipe_refresh_layout)

    "implementation"(Kotlin.coroutines_core)
    "implementation"(Kotlin.coroutines_android)
    "implementation"(Kotlin.datetime)

    "implementation"(MaterialDialogs.material_dialogs)

    "implementation"(ScalingPixels.scaling_pixels)
    "implementation"(Timber.timber)


    "testImplementation"(project(Modules.coreDatastoreTest))
    "testImplementation"(AndroidTestDependencies.coroutines_test)
    "testImplementation"(AndroidTestDependencies.mockk_android)
    "testImplementation"(AndroidTestDependencies.test_arch_core)

    "testImplementation"(TestDependencies.jupiter_engine)
    "testImplementation"(TestDependencies.jupiter_api)
    "testImplementation"(TestDependencies.jupiter_params)
    "testImplementation"(TestDependencies.junit4)
}

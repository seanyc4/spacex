apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(Plugins.android_library)
}

android {
    namespace = Modules.core_test_namespace
}

dependencies {
    implementation(KotlinTest.coroutines_test)
    implementation(AndroidTestDependencies.test_arch_core)
    implementation(AndroidTestDependencies.jupiter_api)
    implementation(ComposeTest.compuse_ui_test_junit4)
    implementation(AndroidTestDependencies.test_runner)
    implementation(HiltTest.hilt_android_testing)
}
apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
}

plugins {
    id("com.android.application")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin(KotlinPlugins.serialization) version Kotlin.kotlin_version
}

android {
    namespace = Modules.app_namespace
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName
        testInstrumentationRunner = AndroidTestDependencies.instrumentation_runner
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["enableCrashReporting"] = false
        }
        getByName("release") {
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$project.rootDir/tools/proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = Java.java_compile_version
        targetCompatibility = Java.java_compile_version
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.compose_compiler_version
    }

    kotlinOptions {
        jvmTarget = Java.java_version
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("test").resources.srcDir("src/test/res")
    }

    testOptions {
        // Fix for mock issue on >= API 28
        packagingOptions.jniLibs.useLegacyPackaging = true

        // Enable test orchestrator
        execution = "ANDROIDX_TEST_ORCHESTRATOR"

        // To prevent textUtils error with espresso idling resource
        unitTests.isReturnDefaultValues = true

        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }

    lint {
        checkDependencies = true
    }
}

dependencies {

    implementation(project(Modules.core))
    implementation(project(Modules.core_datastore))
    implementation(project(Modules.core_database))
    implementation(project(Modules.core_ui))
    implementation(project(Modules.launch_api))
    implementation(project(Modules.launch_impl))

    implementation(AndroidX.app_compat)
    implementation(AndroidX.core_ktx)

    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.lifecycle_live_data_ktx)
    implementation(AndroidX.lifecycle_vm_ktx)
    implementation(AndroidX.lifecycle_saved_state)
    implementation(AndroidX.lifecycle_compose_viewmodel)
    implementation(AndroidX.navigation_dynamic)
    implementation(AndroidX.navigation_fragment)
    implementation(AndroidX.navigation_ui)
    kapt(AndroidX.lifecycle_compiler)
    implementation(AndroidX.splash_screen)
    debugImplementation(AndroidXTest.fragment_testing)
    androidTestImplementation(AndroidXTest.navigation_testing)

    implementation(Compose.compose_runtime)

    androidTestImplementation(HiltTest.hilt_android_testing)
    kaptAndroidTest(Hilt.compiler)

    implementation(Kotlin.serialization)

    implementation(MaterialDialogs.material_dialogs)

    //debugImplementation(Square.leak_canary)
    implementation(Room.room_runtime)
    implementation(Square.retrofit_gson)

    implementation(ScalingPixels.scaling_pixels)
    implementation(Timber.timber)

    implementation(AndroidTestDependencies.idling_resource)
    androidTestImplementation(AndroidTestDependencies.androidx_test_ext)
    androidTestImplementation(AndroidTestDependencies.coroutines_test)
    androidTestImplementation(AndroidTestDependencies.espresso_contrib)
    androidTestImplementation(AndroidTestDependencies.espresso_core)
    androidTestImplementation(AndroidTestDependencies.espresso_intents)
    androidTestImplementation(AndroidTestDependencies.idling_resource)
    androidTestImplementation(AndroidTestDependencies.kotlin_test)
    androidTestImplementation(AndroidTestDependencies.mockk_android)
    androidTestUtil(AndroidTestDependencies.test_orchestrator)
    androidTestImplementation(AndroidTestDependencies.test_rules)
    androidTestImplementation(AndroidTestDependencies.test_runner)
    androidTestImplementation(AndroidTestDependencies.test_core_ktx)
    androidTestImplementation(AndroidTestDependencies.test_arch_core)
}

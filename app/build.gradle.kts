apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(Plugins.android_application)
    id(Plugins.ksp)
    id(Plugins.kotlin_parcelize)
    id(Plugins.hilt)
    kotlin(Plugins.android)
    kotlin(Plugins.serialization) version Kotlin.kotlin_version
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
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$project.rootDir/tools/proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
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

        // To prevent textUtils error with espresso idling resource
        unitTests.isReturnDefaultValues = true

        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    packaging {
                resources.pickFirsts.add("META-INF/LICENSE.md")
                resources.pickFirsts.add("META-INF/LICENSE-notice.md")

        }

    lint {
        checkDependencies = true
    }
}

dependencies {

    implementation(projects.core)
    implementation(projects.coreDatastore)
    implementation(projects.coreDatabase.api)
    implementation(projects.coreDatabase.implementation)
    implementation(projects.coreUi)
    implementation(projects.launch.api)
    implementation(projects.launch.implementation)
    androidTestImplementation(projects.coreTesting)

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
    ksp(AndroidX.lifecycle_compiler)
    implementation(AndroidX.splash_screen)
    implementation(Compose.compose_runtime)
    implementation(Kotlin.serialization)
    implementation(MaterialDialogs.material_dialogs)
    //debugImplementation(Square.leak_canary)
    implementation(Room.room_runtime)
    implementation(Square.retrofit_gson)
    implementation(ScalingPixels.scaling_pixels)
    implementation(Timber.timber)
    implementation(AndroidTestDependencies.idling_resource)

    androidTestImplementation(AndroidTestDependencies.androidx_test_ext)
    androidTestImplementation(AndroidTestDependencies.espresso_core)
    androidTestImplementation(AndroidTestDependencies.espresso_intents)
    androidTestImplementation(AndroidTestDependencies.idling_resource)
    androidTestImplementation(AndroidTestDependencies.mockk_android)
    androidTestImplementation(AndroidTestDependencies.test_rules)
    androidTestImplementation(AndroidTestDependencies.test_runner)
    androidTestImplementation(AndroidTestDependencies.test_core_ktx)
    androidTestImplementation(AndroidTestDependencies.test_arch_core)
    androidTestImplementation(AndroidXTest.navigation_testing)
    androidTestImplementation(KotlinTest.coroutines_test)
    androidTestImplementation(KotlinTest.kotlin_test)

    debugImplementation(AndroidXTest.fragment_testing)
    debugImplementation(AndroidTestDependencies.test_core_monitor)

    androidTestImplementation(ComposeTest.compuse_ui_test)
    androidTestImplementation(ComposeTest.compuse_ui_test_junit4)
    debugImplementation(ComposeTest.compuse_ui_test_manifest)

    androidTestImplementation(projects.coreDatastoreTest)
}
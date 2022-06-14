plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        // Enable Java 8 time below api 26
        isCoreLibraryDesugaringEnabled = true

    }

    kotlinOptions {
        jvmTarget = Java.java_version
    }

    buildFeatures {
        viewBinding = true
        dataBinding = false
    }

    sourceSets {
        getByName("test").resources.srcDir("src/test/res")
    }

    testOptions {
        // Fix for espresso issue on >= API 28
        packagingOptions.jniLibs.useLegacyPackaging = true

        // Enable test orchestrator
        execution = "ANDROIDX_TEST_ORCHESTRATOR"

        // To prevent textUtils error with espresso idling resource
        unitTests.isReturnDefaultValues = true

        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    lint {
        checkDependencies = true
    }

}

dependencies {

    implementation(project(Modules.core))
    implementation(project(Modules.constants))
    implementation(project(Modules.launchDomain))
    implementation(project(Modules.launchInteractors))
    implementation(project(Modules.uiBase))
    implementation(project(Modules.launchViewState))

    implementation(AndroidX.app_compat)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.data_store)
    coreLibraryDesugaring(AndroidX.desurgar)
    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.lifecycle_live_data_ktx)
    implementation(AndroidX.lifecycle_vm_ktx)
    implementation(AndroidX.lifecycle_saved_state)
    implementation(AndroidX.navigation_dynamic)
    implementation(AndroidX.navigation_fragment)
    implementation(AndroidX.navigation_ui)
    kapt(AndroidX.lifecycle_compiler)
    debugImplementation(AndroidXTest.fragment_testing)
    androidTestImplementation(AndroidXTest.navigation_testing)

    implementation(Glide.glide)
    kapt(Glide.glide_compiler)

    implementation(Google.constraint_layout)
    implementation(Google.card_view)
    implementation(Google.material)
    implementation(Google.recycler_view)
    implementation(Google.swipe_refresh_layout)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    androidTestImplementation(HiltTest.hilt_android_testing)
    kaptAndroidTest(Hilt.compiler)

    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
    implementation(Kotlin.datetime)

    implementation(MaterialDialogs.material_dialogs)

    implementation(ScalingPixels.scaling_pixels)
    implementation(Timber.timber)


}

kapt {
    correctErrorTypes = true
}

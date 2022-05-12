plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

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
        // Fix for mock issue on >= API 28
        packagingOptions.jniLibs.useLegacyPackaging = true

        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    lint {
        checkDependencies = true
    }

}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation(AndroidX.app_compat)
    implementation(AndroidX.core_ktx)
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

    implementation(Insetter.insetter)

    implementation(Kotlinx.coroutines_core)
    implementation(Kotlinx.coroutines_android)
    implementation(Kotlinx.datetime)

    //debugImplementation(LeakCanary.leak_canary)
    implementation(MaterialDialogs.material_dialogs)

    implementation(Retrofit.ok_http)
    testImplementation(Retrofit.mock_web_server)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofit_gson)

    implementation(Room.room_ktx)
    implementation(Room.room_runtime)
    kapt(Room.room_compiler)
    implementation(Room.room_migration)
    implementation(Room.room_paging3)

    implementation(SplashScreen.splash_screen)
    implementation(ScalingPixels.scaling_pixels)
    implementation(Timber.timber)

    implementation(AndroidTestDependencies.idling_resource)
    testImplementation(TestDependencies.junit4)
    testImplementation(TestDependencies.jupiter_api)
    testImplementation(TestDependencies.jupiter_params)
    testImplementation(TestDependencies.mockk)
    testRuntimeOnly(TestDependencies.jupiter_engine)

    androidTestImplementation(AndroidTestDependencies.androidx_test_ext)
    androidTestImplementation(AndroidTestDependencies.coroutines_test)
    androidTestImplementation(AndroidTestDependencies.espresso_contrib)
    androidTestImplementation(AndroidTestDependencies.espresso_core)
    androidTestImplementation(AndroidTestDependencies.idling_resource)
    androidTestImplementation(AndroidTestDependencies.kotlin_test)
    androidTestImplementation(AndroidTestDependencies.mockk_android)
    androidTestImplementation(AndroidTestDependencies.test_rules)
    androidTestImplementation(AndroidTestDependencies.test_runner)
    androidTestImplementation(AndroidTestDependencies.text_core_ktx)

}

kapt {
    correctErrorTypes = true
}

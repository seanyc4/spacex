apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    id(libs.plugins.androidApplication.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
    id(libs.plugins.kotlinParcelize.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
    kotlin(libs.plugins.android.get().pluginId)
    kotlin(libs.plugins.kotlinSerializationPlugin.get().pluginId)  version libs.versions.kotlin
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
        testInstrumentationRunner = libs.plugins.hiltTestRunner.get().pluginId
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
    androidTestImplementation(projects.coreTest)

    implementation(libs.appcompat)
    implementation(libs.coreKtx)
    implementation(libs.fragmentKtx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedataktx)
    implementation(libs.lifecycle.savedstate)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.dynamic)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    ksp(libs.lifecycle.compiler)
    implementation(libs.splashScreen)
    implementation(libs.compose.runtime)
    // debugImplementation(libs.squareLeakcanary)
    implementation(libs.room.runtime)
    implementation(libs.googleGson)
    implementation(libs.timber)
    // androidTestImplementation(libs.idlingResource)

    // Android Test Dependencies
    androidTestImplementation(libs.androidxTestExt)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.espressoIntents)
    androidTestImplementation(libs.idlingResource)
    androidTestImplementation(libs.mockkAndroid)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testCoreKtx)
    androidTestImplementation(libs.testArchCore)
    androidTestImplementation(libs.kotlin.coroutinestest)
    androidTestImplementation(libs.kotlin.junit)
    debugImplementation(libs.testCoreMonitor)

    // Compose Test Libraries
    androidTestImplementation(libs.compose.uiTest)
    androidTestImplementation(libs.compose.uiTestJunit4)
    debugImplementation(libs.compose.uiTestManifest)

}
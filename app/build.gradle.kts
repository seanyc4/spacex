apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.seancoyle.spacex"
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
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["enableCrashReporting"] = false
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles("benchmark-rules.pro")
        }
    }

    baselineProfile {
        saveInSrc = true
        automaticGenerationDuringBuild = false
        dexLayoutOptimization = true
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }

    sourceSets {
        getByName("test").resources.srcDir("src/test/res")
    }

    packaging {
        resources.pickFirsts.add("META-INF/LICENSE.md")
        resources.pickFirsts.add("META-INF/LICENSE-notice.md")
    }

    lint {
        checkDependencies = true
        xmlReport = true
    }
}

dependencies {
    baselineProfile(projects.benchmark)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.implementation)
    implementation(projects.core.datastoreProto)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.test)
    implementation(projects.core.ui)
    implementation(projects.database)
    implementation(projects.feature.launch.implementation)

    implementation(libs.appCompat)
    implementation(libs.compose.runtime)
    implementation(libs.coreKtx)
    implementation(libs.fragmentKtx)
    implementation(libs.lifecycle.viewmodelKtx)
    implementation(libs.lifecycle.livedataktx)
    implementation(libs.lifecycle.savedstate)
    ksp(libs.lifecycle.compiler)
    implementation(libs.navigation.dynamic)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.profileInstaller)
    implementation(libs.room.runtime)
    implementation(libs.splashScreen)
    implementation(libs.timber)
    // debugImplementation(libs.squareLeakcanary)

    // Android Test Dependencies
    androidTestImplementation(projects.core.hiltUiTest)
    androidTestImplementation(projects.core.test)
    androidTestImplementation(libs.bundles.androidTestBundle)
    testImplementation(libs.bundles.unitTestBundle)
    debugImplementation(libs.compose.uiTestManifest)
    implementation(libs.compose.uiTestManifest)
    debugImplementation(projects.core.hiltUiTest)

}

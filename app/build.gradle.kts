apply {
    from("$rootDir/hilt.gradle")
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
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.seancoyle.orbital"
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

            val revenueCatKey = (project.findProperty("REVENUECAT_API_KEY_DEBUG") as String?)
            buildConfigField("String", "REVENUECAT_API_KEY", "\"$revenueCatKey\"")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val revenueCatKey = System.getenv("REVENUECAT_API_KEY_PROD")
            buildConfigField("String", "REVENUECAT_API_KEY", "\"$revenueCatKey\"")
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
        compose = true
        buildConfig = true
    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_reports")
        metricsDestination = layout.buildDirectory.dir("compose_metrics")
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
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }

    lint {
        checkDependencies = true
        xmlReport = true
    }
}

dependencies {
    ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.0") // temp fix until dagger support kotlin 2.3.0
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
    implementation(projects.feature.launch)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.coreKtx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.googleAds)
    implementation(libs.lifecycle.savedstate)
    implementation(libs.profileInstaller)
    implementation(libs.revenueCat)
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

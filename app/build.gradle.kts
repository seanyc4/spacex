apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
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
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["enableCrashReporting"] = false
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "consumer-proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.implementation)
    implementation(projects.core.datastoreProto)
    implementation(projects.core.domain)
    implementation(projects.core.test)
    implementation(projects.core.ui)
    implementation(projects.database)
    implementation(projects.feature.launch.api)
    implementation(projects.feature.launch.implementation)
    implementation(projects.feature.launch.test)

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

import com.android.build.api.dsl.LintOptions

apply {
    from("$rootDir/hilt.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
}

plugins {
    alias(libs.plugins.compose.compiler)
    id(libs.plugins.androidApplication.get().pluginId)
    id(libs.plugins.kotlinKsp.get().pluginId)
    id(libs.plugins.kotlinParcelize.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
    kotlin(libs.plugins.android.get().pluginId)
    kotlin(libs.plugins.kotlinSerializationPlugin.get().pluginId) version libs.versions.kotlin
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
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "consumer-proguard-rules.pro"
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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


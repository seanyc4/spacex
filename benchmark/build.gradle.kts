apply{
    from("$rootDir/android-base.gradle")
}

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.seancoyle.benchmark"
    defaultConfig {
        minSdk = 29
        compileSdk = Android.compileSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
            proguardFiles("benchmark-rules.pro")
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
    // Don't use a connected device but rely on a GMD for consistency between local and CI builds.
    useConnectedDevices = true
}

dependencies {
    implementation(libs.junit)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
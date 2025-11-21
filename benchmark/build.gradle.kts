import com.android.build.api.dsl.ManagedVirtualDevice

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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    // Setup GMD for running the baseline profile generation
    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "default"
        }
    }
}

baselineProfile {
    managedDevices.clear()
    managedDevices += "pixel6Api34"

    // If using a connected device it needs to be rooted or API 33 and higher.
    useConnectedDevices = false
}

dependencies {
    implementation(projects.feature.launch.api)
    implementation(libs.junit)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

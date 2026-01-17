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

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // https://issuetracker.google.com/issues/351582215?pli=1
        testInstrumentationRunnerArguments["androidx.benchmark.killProcessDelayMillis"] = "10000"
    }

    // Specifies that baseline profiles should be generated for the :app module
    targetProjectPath = ":app"

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }

    experimentalProperties["android.experimental.self-instrumenting"] = true

    // Setup GMD for running the baseline profile generation
    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "google"
            require64Bit = true
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
    implementation(projects.core.test)
    implementation(libs.junit)
    implementation(libs.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

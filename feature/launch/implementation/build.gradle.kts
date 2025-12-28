apply {
    from("$rootDir/android-base.gradle")
    from("$rootDir/android-base-ui.gradle")
    from("$rootDir/android-base-compose.gradle")
    from("$rootDir/hilt.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.seancoyle.feature.launch.implementation"

    defaultConfig {
        testInstrumentationRunner = libs.plugins.hiltTestRunner.get().pluginId
    }

    packaging {
        resources.pickFirsts.add("META-INF/LICENSE.md")
        resources.pickFirsts.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastoreProto)
    implementation(projects.core.navigation)
    implementation(projects.core.ui)
    implementation(projects.database)
    api(projects.feature.launch.api)

    implementation(libs.appCompat)
    implementation(libs.coreKtx)
    implementation(libs.glideCompose)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.immutable)
    implementation(libs.kotlin.serialization)
    implementation(libs.paging3.runtime)
    implementation(libs.paging3.compose)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.serialization)
    implementation(libs.square.loggingInterceptor)
    implementation(libs.compose.runtime.saveable)

    androidTestImplementation(projects.core.test)
    androidTestImplementation(projects.core.datastore.api)
    androidTestImplementation(libs.bundles.androidTestBundle)
    testImplementation(projects.core.test)
    testImplementation(libs.bundles.unitTestBundle)
}
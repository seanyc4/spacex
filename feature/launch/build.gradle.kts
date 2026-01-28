apply(from = "$rootDir/android-base.gradle")
apply(from = "$rootDir/android-base-compose.gradle")
apply(from = "$rootDir/hilt.gradle")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.seancoyle.feature.launch"

    defaultConfig {
        testInstrumentationRunner = libs.plugins.hiltTestRunner.get().pluginId
    }

    packaging {
        resources.pickFirsts.add("META-INF/LICENSE.md")
        resources.pickFirsts.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastoreProto)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.test)
    implementation(projects.core.ui)
    implementation(projects.database)

    implementation(libs.coreKtx)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
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